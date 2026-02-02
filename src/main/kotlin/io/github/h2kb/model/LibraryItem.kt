package io.github.h2kb.model

interface LibraryItem {
    val title: String
    val year: Int
    val pages: Int

    fun getType(): String
}