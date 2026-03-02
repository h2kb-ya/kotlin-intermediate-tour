package io.github.h2kb.model

enum class ItemCategory(val displayName: String) {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    SCIENCE("Science"),
    TECHNOLOGY("Technology"),
    MAGAZINE("Magazine"),
    JOURNAL("Journal"),
    REFERENCE("Reference");

    companion object {
        fun fromString(value: String): ItemCategory? {
            return entries.find {
                it.name.equals(value, ignoreCase = true)
            }
        }
    }
}