package io.github.h2kb.extention

import io.github.h2kb.model.Book
import io.github.h2kb.model.LibraryItem
import io.github.h2kb.model.Magazine

fun Book.readingTime(): Int = pages * 2 // assuming 2 minutes per page

fun List<Book>.filterByAuthor(author: String): List<Book> = filter { it.author.equals(author, ignoreCase = true) }

fun List<LibraryItem>.publishedAfter(year: Int): List<LibraryItem> = filter { it.year > year }

fun LibraryItem.displayInfo(): String = when (this) {
    is Book -> "Book: $title by $author, published in $year, $pages pages"
    is Magazine -> "Magazine: $title, Issue #$issueNumber, published in $year, $pages pages"
    else -> "Library Item: $title, published in $year, $pages pages"
}