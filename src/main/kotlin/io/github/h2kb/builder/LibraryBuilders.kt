package io.github.h2kb.builder

import io.github.h2kb.model.Book
import io.github.h2kb.model.Magazine

class BookBuilder {
    var title: String = ""
    var author: String = ""
    var year: Int = 0
    var pages: Int = 0

    fun build(): Book = Book(title, year, pages, author)
}

class MagazineBuilder {
    var title: String = ""
    var issueNumber: Int = 0
    var year: Int = 0
    var pages: Int = 0

    fun build(): Magazine = Magazine(title, year, pages, issueNumber)
}

fun book(init: BookBuilder.() -> Unit): Book = BookBuilder().apply(init).build()

fun magazine(init: MagazineBuilder.() -> Unit): Magazine = MagazineBuilder().apply(init).build()