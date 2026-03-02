package io.github.h2kb.repository

import io.github.h2kb.model.*
import io.github.h2kb.util.LibraryConfig

class LibraryRepository {
    private val store = mutableListOf<LibraryItem>()

    fun addItem(item: LibraryItem) {
        store.add(item).also {
            println("Added: ${item.getSummary()}")
        }
    }

    fun getAllItems(): List<LibraryItem> = store.toList()

    fun getBooks(): List<Book> = store.filterIsInstance<Book>()

    fun getMagazines(): List<Magazine> = store.filterIsInstance<Magazine>()

    fun findById(id: Int): LibraryItem? {
        return store.find { it.id == id }
    }

    fun getItemTitleById(id: Int): String {
        val item = findById(id)
        return requireNotNull(item) {
            "Item with id=$id must exist in repository"
        }.title
    }

    fun getItemOrDefault(id: Int): LibraryItem {
        return findById(id) ?: LibraryItem(
            id = -1,
            title = "Not Found",
            year = LibraryConfig.CURRENT_YEAR
        )
    }

    fun findByTitle(title: String): LibraryItem? =
        store.find { it.title.equals(title, ignoreCase = true) }?.let { item ->
            println("Found: ${item.getSummary()}")
            item
        }

    fun findBooksByAuthor(author: String): List<Book> {
        return getBooks().filter { book ->
            book.author.equals(author, ignoreCase = true)
        }
    }

    fun findBooksWithoutIsbn(): List<Book> {
        return getBooks().filter { it.isbn == null }
    }

    fun findBooksWithIsbn(): List<Book> {
        return getBooks().filter { it.isbn != null }
    }

    fun getIsbnInfo(bookId: Int): String {
        val book = findById(bookId) as? Book
        return book?.isbn?.let { isbn ->
            "ISBN: $isbn (length: ${isbn.length})"
        } ?: "No ISBN information available"
    }

    fun calculateStatistics(): ItemStatistics {
        val items = getAllItems()
        val books = getBooks()
        val magazines = getMagazines()

        return ItemStatistics(
            totalItems = items.size,
            totalBooks = books.size,
            totalMagazines = magazines.size,
            averageYear = items.map { it.year }.average().toInt(),
            oldestYear = items.minOfOrNull { it.year } ?: 0,
            newestYear = items.maxOfOrNull { it.year } ?: 0,
            totalPages = books.sumOf { it.pages } + magazines.sumOf { it.pages }
        )
    }

    fun groupByYear(): List<YearGroup> {
        return getAllItems()
            .groupBy { it.year }
            .map { (year, items) -> YearGroup(year, items) }
            .sortedBy { it.year }
    }

    fun getAuthorStatistics(): List<AuthorStats> {
        return getBooks()
            .groupBy { it.author }
            .map { (author, books) ->
                AuthorStats(
                    authorName = author,
                    bookCount = books.size,
                    totalPages = books.sumOf { it.pages }
                )
            }
            .sortedByDescending { it.bookCount }
    }

    fun validateAllItems(): List<String> {
        val errors = mutableListOf<String>()

        getAllItems().forEach { item ->
            try {
                requireNotNull(item.title.takeIf { it.isNotBlank() }) {
                    "Item ${item.id} has blank title"
                }

                checkNotNull(item) { "Item cannot be null" }

            } catch (e: IllegalArgumentException) {
                errors.add("Validation error for item ${item.id}: ${e.message}")
            } catch (e: IllegalStateException) {
                errors.add("State error for item ${item.id}: ${e.message}")
            }
        }

        return errors
    }

    fun getStatistics(): String {
        val stats = calculateStatistics()
        return stats.displayReport()
    }

    fun clear() {
        store.clear()
    }

    fun size(): Int = store.size
}