package io.github.h2kb.model

data class Magazine(
    override val title: String,
    override val year: Int,
    override val pages: Int,
    val issueNumber: Int
) : LibraryItem {
    override fun getType() = "Magazine"
}