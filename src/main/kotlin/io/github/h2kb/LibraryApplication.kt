package io.github.h2kb

import io.github.h2kb.model.*
import io.github.h2kb.repository.LibraryRepository
import io.github.h2kb.util.IdGenerator
import io.github.h2kb.util.LibraryConfig

fun main() {
    println("═".repeat(60))
    println("   LIBRARY CATALOG & ANALYZER")
    println("   Kotlin Core Language Features Demonstration")
    println("═".repeat(60))
    println()

    println("┌─ STEP 1: Initializing Repository ──────────────────┐")
    val repository = LibraryRepository()
    IdGenerator.reset()
    println("│ Repository created                                 │")
    println("│ IdGenerator reset                                  │")
    println("└────────────────────────────────────────────────────┘\n")

    println("┌─ STEP 2: Loading Data ─────────────────────────────┐")

    println("│ Creating sample book via companion object...")
    val sampleBook = Book.createSample()
    repository.addItem(sampleBook)

    println("│")
    println("│ Parsing books from CSV via companion object...")
    val csvBooks = listOf(
        "1,Foundation,1951,Isaac Asimov,255,978-0553293357,Foundation Trilogy",
        "2,Dune,1965,Frank Herbert,412,978-0441013593,Desert Planet Saga",
        "3,1984,1949,George Orwell,328,,",
        "4,Clean Code,2008,Robert C. Martin,464,978-0132350884,A Handbook of Agile Software Craftsmanship"
    )

    csvBooks.forEach { csv ->
        Book.fromCsv(csv)?.let { book ->
            repository.addItem(book)
        } ?: println("│ ✗ Failed to parse: $csv")
    }

    println("│")
    println("│ Creating magazines via companion methods...")

    val scienceQuarterly = Magazine.createQuarterly(
        "Science Quarterly", 2024, 4, "SciPress"
    )
    repository.addItem(scienceQuarterly)

    val natGeo = Magazine.createMonthly(
        "National Geographic", 2024, 3, "National Geographic Society"
    )
    repository.addItem(natGeo)

    val techMagazine = Magazine(
        magazineId = IdGenerator.nextId(),
        magazineTitle = "Tech Monthly",
        magazineYear = 2024,
        issueNumber = 15,
        pages = 120,
        publisher = "TechMedia",
        month = "March"
    )
    repository.addItem(techMagazine)

    println("└────────────────────────────────────────────────────┘\n")

    println("┌─ SCENARIO A: Items Grouped by Publication Year ────┐")
    val yearGroups = repository.groupByYear()

    yearGroups.forEach { group ->
        println("│")
        println("│ ${group.summary()}")
        group.items.forEach { item ->
            when (item) {
                is Book -> {
                    val subtitle = item.subtitle ?: "(no subtitle)"
                    println("│      ${item.displayTitle}")
                    println("│      $subtitle")
                    println("│      Author: ${item.author}")
                    println("│      Age: ${item.age} years")
                    println("│      Reading time: ~${item.readingTime} minutes")
                }

                is Magazine -> {
                    println("│      ${item.fullTitle}")
                    println("│      ${item.displayIssue} - ${item.publicationDate}")
                }
            }
        }
    }
    println("└────────────────────────────────────────────────────┘\n")

    println("┌─ SCENARIO B: Books with Missing Optional Fields ───┐")

    val booksWithoutIsbn = repository.findBooksWithoutIsbn()
    println("│")
    println("│ Books without ISBN: ${booksWithoutIsbn.size}")
    booksWithoutIsbn.forEach { book ->
        val isbnStatus = book.isbn?.let { "ISBN: $it" } ?: "️  No ISBN"
        println("│   ${book.title}")
        println("│      Status: $isbnStatus")

        println("│      ${book.formattedIsbn}")
    }

    println("│")
    val booksWithIsbn = repository.findBooksWithIsbn()
    println("│ Books with ISBN: ${booksWithIsbn.size}")
    booksWithIsbn.forEach { book ->
        book.isbn?.let { isbn ->
            println("│   ${book.title}: $isbn (${isbn.length} chars)")
        }
    }

    println("└────────────────────────────────────────────────────┘\n")

    println("┌─ SCENARIO C: Books Sorted by Publication Year ─────┐")
    val sortedBooks = repository.getBooks().sortedBy { it.year }

    sortedBooks.forEach { book ->
        println("│ ${book.year}: ${book.displayTitle}")
        println("│    ${book.authorInfo}")
        println("│    Age: ${book.age} years, Pages: ${book.pages}")

        book.subtitle?.let {
            println("│    Subtitle: $it")
        }
        println("│")
    }
    println("└────────────────────────────────────────────────────┘\n")

    println("┌─ SCENARIO D: Item Count by Type ───────────────────┐")
    val bookCount = repository.getBooks().size
    val magazineCount = repository.getMagazines().size
    val totalCount = repository.size()

    println("│ Total Items:     $totalCount")
    println("│ Books:           $bookCount (${(bookCount * 100 / totalCount)}%)")
    println("│ Magazines:       $magazineCount (${(magazineCount * 100 / totalCount)}%)")

    println("│")
    println("│ Items by type:")
    repository.getAllItems()
        .groupBy { it.getType() }
        .forEach { (type, items) ->
            println("│   $type: ${items.size}")
        }

    println("└────────────────────────────────────────────────────┘\n")

    println("┌─ BONUS: Author Statistics ─────────────────────────┐")
    val authorStats = repository.getAuthorStatistics()

    authorStats.forEach { stats ->
        println("│ ${stats.authorName}")
        println("│   Books: ${stats.bookCount}")
        println("│   Total pages: ${stats.totalPages}")
        println("│   Average pages: ${stats.averagePages}")
        println("│")
    }
    println("└────────────────────────────────────────────────────┘\n")

    println("┌─ VALIDATION: Checking Data Integrity ──────────────┐")

    repository.getAllItems().forEach { item ->
        try {
            val validTitle = requireNotNull(item.title.takeIf { it.isNotBlank() }) {
                "Item ${item.id} has invalid title"
            }
            println("│   Item #${item.id}: '$validTitle' - valid")

            require(LibraryConfig.validateYear(item.year)) {
                "Invalid year for item ${item.id}"
            }

        } catch (e: IllegalArgumentException) {
            println("│   Validation error: ${e.message}")
        }
    }

    val errors = repository.validateAllItems()
    if (errors.isEmpty()) {
        println("│")
        println("│   All items passed validation!")
    } else {
        println("│")
        println("│   Found ${errors.size} validation errors:")
        errors.forEach { error ->
            println("│   - $error")
        }
    }

    println("└────────────────────────────────────────────────────┘\n")

    println("┌─ SEALED CLASS Demo: Parsing Results ───────────────┐")

    val testCsvLines = listOf(
        "100,Test Book,2020,Test Author,200,123-456,Test Subtitle",
        "101,Invalid Book,3000,Author,100,,",
        "",
        "invalid,line,format"
    )

    testCsvLines.forEach { csv ->
        val result = Book.fromCsvResult(csv)

        when (result) {
            is ParseResult.Success -> {
                println("│   Success: ${result.item.title}")
            }

            is ParseResult.Error -> {
                println("│   Error: ${result.message}")
                println("│   Line: ${result.line}")
            }

            ParseResult.EmptyInput -> {
                println("│   Empty input detected")
            }
        }
    }

    println("└────────────────────────────────────────────────────┘\n")

    println("┌─ FINAL STATISTICS REPORT ──────────────────────────┐")
    val stats = repository.calculateStatistics()
    println(stats.displayReport())
    println("└────────────────────────────────────────────────────┘\n")

    println("┌─ CONFIGURATION INFO ───────────────────────────────┐")
    println("│ Current Year: ${LibraryConfig.CURRENT_YEAR}")
    println("│ Pages per minute: ${LibraryConfig.DEFAULT_PAGES_PER_MINUTE}")
    println("│ Max title length: ${LibraryConfig.MAX_TITLE_LENGTH}")
    println("└────────────────────────────────────────────────────┘\n")

    println("═".repeat(60))
    println("   Analysis Complete!")
    println("═".repeat(60))
}