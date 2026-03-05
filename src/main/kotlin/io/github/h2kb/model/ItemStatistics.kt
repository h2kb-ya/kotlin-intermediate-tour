package io.github.h2kb.model

data class ItemStatistics(
    val totalItems: Int,
    val totalBooks: Int,
    val totalMagazines: Int,
    val averageYear: Int,
    val oldestYear: Int,
    val newestYear: Int,
    val totalPages: Int
) {
    fun displayReport(): String = """
        |╔════════════════════════════════════╗
        |║     Library Statistics Report      ║
        |╠════════════════════════════════════╣
        |║ Total Items:      ${totalItems.toString().padStart(6)} ║
        |║ Books:            ${totalBooks.toString().padStart(6)} ║
        |║ Magazines:        ${totalMagazines.toString().padStart(6)} ║
        |║ Total Pages:      ${totalPages.toString().padStart(6)} ║
        |║ Average Year:     ${averageYear.toString().padStart(6)} ║
        |║ Oldest Year:      ${oldestYear.toString().padStart(6)} ║
        |║ Newest Year:      ${newestYear.toString().padStart(6)} ║
        |╚════════════════════════════════════╝
    """.trimMargin()
}

data class YearGroup(
    val year: Int,
    val items: List<LibraryItem>,
    val count: Int = items.size
) {
    fun summary(): String = "$year: $count items"
}

data class AuthorStats(
    val authorName: String,
    val bookCount: Int,
    val totalPages: Int,
    val averagePages: Int = if (bookCount > 0) totalPages / bookCount else 0
)