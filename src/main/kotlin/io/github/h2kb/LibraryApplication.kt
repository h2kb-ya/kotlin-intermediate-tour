package io.github.h2kb

import io.github.h2kb.builder.book
import io.github.h2kb.builder.magazine
import io.github.h2kb.extention.displayInfo
import io.github.h2kb.extention.filterByAuthor
import io.github.h2kb.extention.publishedAfter
import io.github.h2kb.extention.readingTime
import io.github.h2kb.model.Book
import io.github.h2kb.model.Magazine
import io.github.h2kb.repository.LibraryRepository

fun main() {
    println("=".repeat(60))
    println("Library Management System")
    println("=".repeat(60))
    println()

    val repository = LibraryRepository().apply {
        println("\n--- Repository Initialized ---")
    }

    println()

    val dune = book {
        title = "Dune"
        author = "Frank Herbert"
        year = 1965
        pages = 412
    }

    val foundation = book {
        title = "Foundation"
        author = "Isaac Asimov"
        year = 1951
        pages = 255
    }

    val scienceMag = magazine {
        title = "Science Weekly"
        issueNumber = 42
        year = 2023
    }

    val techMag = Magazine("Tech Monthly", 15, 2022, 13)
    val warAndPeace = Book("War and Peace", 1869, 1225, "Leo Tolstoy")

    println("\n--- Adding items to repository ---")
    repository.apply {
        addItem(dune)
        addItem(foundation)
        addItem(scienceMag)
        addItem(techMag)
        addItem(warAndPeace)
    }

    println("\n--- Items information ---")
    repository.getAllItems().forEach { item ->
        println(item.displayInfo())

        (item as? Book)?.let { book ->
            val time = book.readingTime()
            println("\nEstimated reading time: $time minutes")
        }
    }

    println("\n--- Book filtering ---")
    with(repository.getBooks()) {
        println("Total books: $size")

        val recentBooks = publishedAfter(1950)
        println("\nBooks published after 1950:")
        recentBooks.forEach { println("   • ${it.title} (${it.year})") }
    }

    println("\n--- Find ---")
    repository.findByTitle("Dune")

    println("\n--- Statistics ---")
    println(repository.getStatistics())

    println("\n--- Data processing ---")
    repository.getBooks()
        .filterByAuthor("Isaac Asimov")
        .also { books -> println("Books found for Asimov: ${books.size}") }
        .forEach { println("   ${it.title}") }

    println("\n" + "=".repeat(60))
}
