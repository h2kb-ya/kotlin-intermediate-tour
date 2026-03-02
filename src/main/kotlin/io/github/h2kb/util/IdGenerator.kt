package io.github.h2kb.util

object IdGenerator {
    private var currentId = 0
    fun nextId(): Int {
        return ++currentId
    }

    fun reset() {
        currentId = 0
    }

    fun getCurrentId(): Int = currentId
}
