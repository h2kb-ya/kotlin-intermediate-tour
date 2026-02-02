package io.github.h2kb.model

data class Book(
    override val title: String,
    override val year: Int,
    override val pages: Int,
    val author: String
) : LibraryItem {
    override fun getType() = "Book"
}