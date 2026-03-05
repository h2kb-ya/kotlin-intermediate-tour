package io.github.h2kb.model

import io.github.h2kb.util.LibraryConfig

open class LibraryItem(
    val id: Int,
    val title: String,
    val year: Int
) {
    val age: Int
        get() = LibraryConfig.CURRENT_YEAR - year

    open fun getType(): String = "Generic Library Item"

    open fun getSummary(): String = "$title ($year)"

    init {
        require(LibraryConfig.validateYear(year)) {
            "Invalid year: $year. Must be between 1000 and ${LibraryConfig.CURRENT_YEAR}"
        }
        require(LibraryConfig.validateTitle(title)) {
            "Invalid title: must be between ${LibraryConfig.MIN_TITLE_LENGTH} " +
                    "and ${LibraryConfig.MAX_TITLE_LENGTH} characters"
        }
    }

    override fun toString(): String = getSummary()
}