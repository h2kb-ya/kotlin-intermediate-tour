package io.github.h2kb.util

object LibraryConfig {
    const val CURRENT_YEAR = 2026
    const val MAX_TITLE_LENGTH = 200
    const val MIN_TITLE_LENGTH = 1
    const val DEFAULT_PAGES_PER_MINUTE = 2
    const val MIN_PAGES = 1
    const val MAX_PAGES = 10000

    private const val MIN_VALID_YEAR = 1000

    val defaultCategories = listOf(
        "Fiction",
        "Non-Fiction",
        "Science",
        "Technology"
    )

    fun validateYear(year: Int): Boolean {
        return year in MIN_VALID_YEAR..CURRENT_YEAR
    }

    fun validateTitle(title: String): Boolean {
        val trimmed = title.trim()
        return trimmed.isNotBlank() &&
                trimmed.length in MIN_TITLE_LENGTH..MAX_TITLE_LENGTH
    }

    fun validatePages(pages: Int): Boolean {
        return pages in MIN_PAGES..MAX_PAGES
    }

    fun estimateReadingTime(pages: Int): Int {
        return pages * DEFAULT_PAGES_PER_MINUTE
    }
}