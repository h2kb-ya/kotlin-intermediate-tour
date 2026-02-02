package io.github.h2kb.repository

import io.github.h2kb.model.Book
import io.github.h2kb.model.LibraryItem
import io.github.h2kb.model.Magazine

class LibraryRepository {
    private val store = mutableListOf<LibraryItem>()

    fun addItem(item: LibraryItem) {
        store.add(item).also {
            println("Added item: ${item.title} (${item.getType()})")
        }
    }

    fun getAllItems(): List<LibraryItem> {
        return store.toList()
    }

    fun getBooks(): List<Book> = store.filterIsInstance<Book>()

    fun getMagazines(): List<LibraryItem> = store.filterIsInstance<Magazine>()

    fun findByTitle(title: String): LibraryItem? =
        store.find { it.title.equals(title, ignoreCase = true) }?.let { item ->
            println("Found item: ${item.title} (${item.getType()})")
            item
        }

    fun getStatistics(): String = run {
        val totalBooks = getBooks().size
        val totalMagazines = getMagazines().size
        val totalPages = getBooks().sumOf { it.pages }

        """
        Library Statistics:
        - Total items: ${store.size}
        - Books: $totalBooks
        - Magazines: $totalMagazines
        - Total Pages: $totalPages
        """.trimIndent()
    }
}