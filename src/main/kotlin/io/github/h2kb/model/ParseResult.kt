package io.github.h2kb.model

sealed class ParseResult {
    data class Success(val item: LibraryItem) : ParseResult()
    data class Error(val message: String, val line: String) : ParseResult()
    object EmptyInput : ParseResult()

    fun isSuccess(): Boolean = this is Success

    fun getItemOrNull(): LibraryItem? = when (this) {
        is Success -> item
        else -> null
    }
}

sealed class SearchResult {
    data class Found(val items: List<LibraryItem>) : SearchResult()
    object NotFound : SearchResult()
    data class InvalidQuery(val reason: String) : SearchResult()
}