package io.github.h2kb.model

import io.github.h2kb.util.IdGenerator
import io.github.h2kb.util.LibraryConfig

data class Book(
    val bookId: Int,
    val bookTitle: String,
    val bookYear: Int,
    val author: String,
    val pages: Int,
    val isbn: String? = null,
    val subtitle: String? = null,
    val category: ItemCategory = ItemCategory.FICTION
) : LibraryItem(bookId, bookTitle, bookYear) {

    override val title: String = bookTitle

    override fun getType(): String = "Book"

    val readingTime: Int
        get() = LibraryConfig.estimateReadingTime(pages)

    val displayTitle: String
        get() = subtitle?.let { "$title: $it" } ?: title

    val formattedIsbn: String
        get() = isbn?.let { "ISBN: $it" } ?: "No ISBN available"

    val authorInfo: String
        get() = "by $author"

    init {
        require(author.isNotBlank()) { "Author cannot be blank" }
        require(LibraryConfig.validatePages(pages)) {
            "Pages must be between ${LibraryConfig.MIN_PAGES} and ${LibraryConfig.MAX_PAGES}"
        }
        // Валидация ISBN если он есть
        isbn?.let {
            require(it.isNotBlank()) { "ISBN cannot be blank if provided" }
        }
    }

    override fun getSummary(): String {
        return "$displayTitle by $author ($bookYear) - $pages pages"
    }

    companion object {
        fun fromCsv(line: String): Book? {
            val parts = line.split(",").map { it.trim() }

            val id = parts.getOrNull(0)?.toIntOrNull() ?: return null
            val title = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: return null
            val year = parts.getOrNull(2)?.toIntOrNull() ?: return null
            val author = parts.getOrNull(3)?.takeIf { it.isNotBlank() } ?: return null
            val pages = parts.getOrNull(4)?.toIntOrNull() ?: return null

            val isbn = parts.getOrNull(5)?.takeIf { it.isNotBlank() }
            val subtitle = parts.getOrNull(6)?.takeIf { it.isNotBlank() }

            return try {
                Book(
                    bookId = id,
                    bookTitle = title,
                    bookYear = year,
                    author = author,
                    pages = pages,
                    isbn = isbn,
                    subtitle = subtitle
                )
            } catch (_: IllegalArgumentException) {
                null
            }
        }

        fun fromCsvResult(line: String): ParseResult {
            if (line.isBlank()) return ParseResult.EmptyInput

            return fromCsv(line)?.let { book ->
                ParseResult.Success(book)
            } ?: ParseResult.Error("Failed to parse book from line", line)
        }

        fun createSample(): Book {
            return Book(
                bookId = IdGenerator.nextId(),
                bookTitle = "The Kotlin Programming Language",
                bookYear = 2024,
                author = "JetBrains Team",
                pages = 450,
                isbn = "978-1234567890",
                subtitle = "A Modern Approach",
                category = ItemCategory.TECHNOLOGY
            )
        }

        fun createSamples(): List<Book> = listOf(
            Book(
                bookId = IdGenerator.nextId(),
                bookTitle = "Dune",
                bookYear = 1965,
                author = "Frank Herbert",
                pages = 412,
                isbn = "978-0441013593",
                subtitle = "Desert Planet Saga",
                category = ItemCategory.FICTION
            ),
            Book(
                bookId = IdGenerator.nextId(),
                bookTitle = "Foundation",
                bookYear = 1951,
                author = "Isaac Asimov",
                pages = 255,
                isbn = "978-0553293357",
                subtitle = null,
                category = ItemCategory.SCIENCE
            ),
            Book(
                bookId = IdGenerator.nextId(),
                bookTitle = "1984",
                bookYear = 1949,
                author = "George Orwell",
                pages = 328,
                isbn = null,
                subtitle = null,
                category = ItemCategory.FICTION
            ),
            Book(
                bookId = IdGenerator.nextId(),
                bookTitle = "Clean Code",
                bookYear = 2008,
                author = "Robert Martin",
                pages = 464,
                isbn = "978-0132350884",
                subtitle = "A Handbook of Agile Software Craftsmanship",
                category = ItemCategory.TECHNOLOGY
            )
        )
    }
}