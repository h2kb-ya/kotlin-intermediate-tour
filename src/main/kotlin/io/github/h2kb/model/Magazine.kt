package io.github.h2kb.model

import io.github.h2kb.util.IdGenerator
import io.github.h2kb.util.LibraryConfig

data class Magazine(
    val magazineId: Int,
    val magazineTitle: String,
    val magazineYear: Int,
    val issueNumber: Int,
    val pages: Int,
    val publisher: String? = null,
    val month: String? = null
) : LibraryItem(magazineId, magazineTitle, magazineYear) {

    override fun getType(): String = "Magazine"

    val displayIssue: String
        get() = "Issue #$issueNumber"

    val fullTitle: String
        get() = publisher?.let { "$it - $title" } ?: title

    val publicationDate: String
        get() = month?.let { "$it $magazineYear" } ?: "$magazineYear"

    init {
        require(issueNumber > 0) { "Issue number must be positive" }
        require(LibraryConfig.validatePages(pages)) {
            "Pages must be between ${LibraryConfig.MIN_PAGES} and ${LibraryConfig.MAX_PAGES}"
        }
    }

    override fun getSummary(): String {
        return "$fullTitle, $displayIssue ($publicationDate)"
    }

    companion object {
        fun createQuarterly(
            title: String,
            year: Int,
            quarter: Int,
            publisher: String? = null
        ): Magazine {
            require(quarter in 1..4) { "Quarter must be between 1 and 4" }

            val month = when (quarter) {
                1 -> "January-March"
                2 -> "April-June"
                3 -> "July-September"
                4 -> "October-December"
                else -> null
            }

            return Magazine(
                magazineId = IdGenerator.nextId(),
                magazineTitle = title,
                magazineYear = year,
                issueNumber = quarter,
                pages = 100,
                publisher = publisher,
                month = month
            )
        }

        fun createMonthly(
            title: String,
            year: Int,
            month: Int,
            publisher: String? = null
        ): Magazine {
            require(month in 1..12) { "Month must be between 1 and 12" }

            val monthName = listOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )[month - 1]

            return Magazine(
                magazineId = IdGenerator.nextId(),
                magazineTitle = title,
                magazineYear = year,
                issueNumber = month,
                pages = 80,
                publisher = publisher,
                month = monthName
            )
        }

        fun fromCsv(line: String): Magazine? {
            val parts = line.split(",").map { it.trim() }

            val id = parts.getOrNull(0)?.toIntOrNull() ?: return null
            val title = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: return null
            val year = parts.getOrNull(2)?.toIntOrNull() ?: return null
            val issueNumber = parts.getOrNull(3)?.toIntOrNull() ?: return null
            val pages = parts.getOrNull(4)?.toIntOrNull() ?: return null
            val publisher = parts.getOrNull(5)?.takeIf { it.isNotBlank() }
            val month = parts.getOrNull(6)?.takeIf { it.isNotBlank() }

            return try {
                Magazine(
                    magazineId = id,
                    magazineTitle = title,
                    magazineYear = year,
                    issueNumber = issueNumber,
                    pages = pages,
                    publisher = publisher,
                    month = month
                )
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        fun createSamples(): List<Magazine> = listOf(
            createMonthly("National Geographic", 2024, 3, "National Geographic Society"),
            createQuarterly("Science Quarterly", 2023, 4, "SciPress"),
            Magazine(IdGenerator.nextId(), "Tech Monthly", 2024, 15, 120, "TechMedia", "March")
        )
    }
}