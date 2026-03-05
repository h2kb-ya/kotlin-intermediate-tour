# 📚 Library Catalog & Analyzer
## Kotlin Core Language Features — Demonstration Project

---

## 🎯 Objective

This project demonstrates mastery of **four core Kotlin language features**:
1. **Objects** (singleton, companion objects)
2. **Open and Special Classes** (open, data, sealed, enum)
3. **Properties** (custom getters/setters, backing fields)
4. **Null Safety** (nullable types, safe calls, Elvis operator, let, requireNotNull)

**Domain**: Library management system for books and magazines.

---

## 🚀 How to Run

### Prerequisites
- Kotlin compiler installed (`kotlinc`)
- Java Runtime Environment (JRE 8 or higher)

### Option 1: Using kotlinc (Command Line)

```bash
# Navigate to project directory
cd kotlin-intermediate-tour

# Compile all Kotlin files
kotlinc -include-runtime -d library-app.jar $(find src -name "*.kt")

# Run the application
java -jar library-app.jar
```

### Option 2: Using IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. Navigate to `src/main/kotlin/io/github/h2kb/LibraryApplication.kt`
3. Click the green "Run" button next to the `main()` function
4. View output in the Run console

### Option 3: Using Gradle (if configured)

```bash
./gradlew run
```

---

## 📋 Project Structure

```
src/main/kotlin/io/github/h2kb/
├── LibraryApplication.kt          # Main entry point
├── model/
│   ├── LibraryItem.kt             # Open base class
│   ├── Book.kt                    # Data class extending LibraryItem
│   ├── Magazine.kt                # Data class extending LibraryItem
│   ├── ItemCategory.kt            # Enum class
│   ├── ParseResult.kt             # Sealed class hierarchy
│   └── ItemStatistics.kt          # Data classes for reports
├── repository/
│   └── LibraryRepository.kt       # Repository pattern
└── util/
    ├── IdGenerator.kt             # Object (singleton)
    └── LibraryConfig.kt           # Object (singleton)
```

---

## 🔍 Kotlin Features Demonstrated

### 1️⃣ Objects

#### **Object Declarations (Singleton)**

**Location**: `util/IdGenerator.kt`, `util/LibraryConfig.kt`

```kotlin
object IdGenerator {
    private var currentId = 0
    fun nextId(): Int = ++currentId
    fun reset() { currentId = 0 }
}

object LibraryConfig {
    const val CURRENT_YEAR = 2026
    const val MAX_TITLE_LENGTH = 200
    fun validateYear(year: Int): Boolean = ...
}
```

**Purpose**: 
- `IdGenerator` — central ID generation service (singleton pattern)
- `LibraryConfig` — application-wide configuration and validation rules

**Why Objects?** These are stateful singletons that need exactly one instance throughout the application lifecycle.

---

#### **Companion Objects**

**Location**: `model/Book.kt` (lines 43-96), `model/Magazine.kt` (lines 37-121)

```kotlin
data class Book(...) {
    companion object {
        // Factory method - parse CSV
        fun fromCsv(line: String): Book? { ... }
        
        // Factory method - parse with result type
        fun fromCsvResult(line: String): ParseResult { ... }
        
        // Sample data creator
        fun createSample(): Book { ... }
        fun createSamples(): List<Book> { ... }
    }
}
```

**Purpose**: Factory methods for creating instances from CSV, sample data generation

**Demonstrated in**: `LibraryApplication.kt` lines 25, 31-42, 51-63

---

### 2️⃣ Open and Special Classes

#### **Open Class**

**Location**: `model/LibraryItem.kt` (lines 5-28)

```kotlin
open class LibraryItem(
    val id: Int,
    val title: String,
    val year: Int
) {
    // Computed property
    val age: Int
        get() = LibraryConfig.CURRENT_YEAR - year
    
    // Open methods for override
    open fun getType(): String = "Generic Library Item"
    open fun getSummary(): String = "$title ($year)"
}
```

**Purpose**: Base class allowing inheritance by `Book` and `Magazine`. Open methods enable polymorphic behavior.

---

#### **Data Classes**

**Location**: `model/Book.kt`, `model/Magazine.kt`, `model/ItemStatistics.kt`

```kotlin
data class Book(...) : LibraryItem(...) { ... }
data class Magazine(...) : LibraryItem(...) { ... }
data class ItemStatistics(...) { ... }
data class YearGroup(...) { ... }
data class AuthorStats(...) { ... }
```

**Purpose**: Auto-generated `equals()`, `hashCode()`, `toString()`, `copy()` for data-centric classes.

**Why Data Classes?** These classes primarily hold data and benefit from structural equality and built-in methods.

---

#### **Sealed Class**

**Location**: `model/ParseResult.kt` (lines 3-13)

```kotlin
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
```

**Purpose**: Type-safe representation of parsing results with exhaustive `when` expressions.

**Demonstrated**: `LibraryApplication.kt` (lines 208-228) — parsing CSV with type-safe results

**Why Sealed?** Restricted class hierarchy ensures all cases are handled in when expressions at compile time.

---

#### **Enum Class**

**Location**: `model/ItemCategory.kt` (lines 3-19)

```kotlin
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
```

**Purpose**: Type-safe categorization of library items.

**Used in**: `Book.kt` (line 14) — each book has a category.

---

### 3️⃣ Properties

#### **Computed Properties (Custom Getters)**

**Location**: `model/LibraryItem.kt` (lines 10-11)

```kotlin
val age: Int
    get() = LibraryConfig.CURRENT_YEAR - year
```

**Purpose**: Calculate item age dynamically without storing redundant data.

---

**Location**: `model/Book.kt` (lines 18-28)

```kotlin
val readingTime: Int
    get() = LibraryConfig.estimateReadingTime(pages)

val displayTitle: String
    get() = subtitle?.let { "$title: $it" } ?: title

val formattedIsbn: String
    get() = isbn?.let { "ISBN: $it" } ?: "No ISBN available"

val authorInfo: String
    get() = "by $author"
```

**Purpose**: 
- `readingTime` — estimated reading duration
- `displayTitle` — combines title with subtitle if available
- `formattedIsbn` — formatted ISBN with fallback
- `authorInfo` — formatted author display

---

**Location**: `model/Magazine.kt` (lines 18-25)

```kotlin
val displayIssue: String
    get() = "Issue #$issueNumber"

val fullTitle: String
    get() = publisher?.let { "$it - $title" } ?: title

val publicationDate: String
    get() = month?.let { "$it $magazineYear" } ?: "$magazineYear"
```

**Purpose**: Dynamic formatting for magazine display information.

---

#### **Properties with Nullable Types**

**Location**: `model/Book.kt` (lines 12-13)

```kotlin
val isbn: String? = null
val subtitle: String? = null
```

**Location**: `model/Magazine.kt` (lines 12-13)

```kotlin
val publisher: String? = null
val month: String? = null
```

**Purpose**: Optional fields that may or may not be present, enforcing null-safety at compile time.

---

### 4️⃣ Null Safety

#### **Nullable Types (`T?`)**

Used throughout `Book`, `Magazine` for optional fields:
- `Book.isbn: String?` — optional ISBN
- `Book.subtitle: String?` — optional subtitle
- `Magazine.publisher: String?` — optional publisher
- `Magazine.month: String?` — optional month

**Also used in return types**:
- `Book.fromCsv(line: String): Book?` — returns null on parse failure
- `LibraryRepository.findById(id: Int): LibraryItem?` — returns null if not found

---

#### **Safe Call Operator (`?.`)**

**Location**: `model/Book.kt` (lines 38-40)

```kotlin
isbn?.let {
    require(it.isNotBlank()) { "ISBN cannot be blank if provided" }
}
```

**Location**: `Book.fromCsv` (lines 50-57)

```kotlin
val isbn = parts.getOrNull(5)?.takeIf { it.isNotBlank() }
val subtitle = parts.getOrNull(6)?.takeIf { it.isNotBlank() }
```

**Location**: `LibraryApplication.kt` (lines 81-85, 109-112)

```kotlin
when (item) {
    is Book -> {
        val subtitle = item.subtitle ?: "(no subtitle)"
        // ...
    }
}

booksWithIsbn.forEach { book ->
    book.isbn?.let { isbn ->
        println("│   ${book.title}: $isbn (${isbn.length} chars)")
    }
}
```

**Purpose**: Safely access properties and methods on nullable references.

---

#### **Elvis Operator (`?:`)**

**Location**: `model/Book.kt` (lines 21-25)

```kotlin
val displayTitle: String
    get() = subtitle?.let { "$title: $it" } ?: title

val formattedIsbn: String
    get() = isbn?.let { "ISBN: $it" } ?: "No ISBN available"
```

**Location**: `model/Magazine.kt` (lines 21-25)

```kotlin
val fullTitle: String
    get() = publisher?.let { "$it - $title" } ?: title

val publicationDate: String
    get() = month?.let { "$it $magazineYear" } ?: "$magazineYear"
```

**Location**: `Book.fromCsv` (lines 50-54)

```kotlin
val id = parts.getOrNull(0)?.toIntOrNull() ?: return null
val title = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: return null
val year = parts.getOrNull(2)?.toIntOrNull() ?: return null
val author = parts.getOrNull(3)?.takeIf { it.isNotBlank() } ?: return null
val pages = parts.getOrNull(4)?.toIntOrNull() ?: return null
```

**Purpose**: Provide default values when left-side expression is null.

---

#### **`let` Function**

**Location**: `model/Book.kt` (lines 21, 24, 38-40)

```kotlin
val displayTitle: String
    get() = subtitle?.let { "$title: $it" } ?: title

val formattedIsbn: String
    get() = isbn?.let { "ISBN: $it" } ?: "No ISBN available"

// Validate ISBN only if present
isbn?.let {
    require(it.isNotBlank()) { "ISBN cannot be blank if provided" }
}
```

**Location**: `repository/LibraryRepository.kt` (lines 43-46)

```kotlin
fun findByTitle(title: String): LibraryItem? =
    store.find { it.title.equals(title, ignoreCase = true) }?.let { item ->
        println("Found: ${item.getSummary()}")
        item
    }
```

**Location**: `LibraryApplication.kt` (lines 39-41)

```kotlin
Book.fromCsv(csv)?.let { book ->
    repository.addItem(book)
} ?: println("│ ✗ Failed to parse: $csv")
```

**Purpose**: Execute code block only if value is non-null, transforming the value.

---

#### **`requireNotNull` / `checkNotNull`**

**Location**: `repository/LibraryRepository.kt` (lines 27-30)

```kotlin
fun getItemTitleById(id: Int): String {
    val item = findById(id)
    return requireNotNull(item) {
        "Item with id=$id must exist in repository"
    }.title
}
```

**Location**: `LibraryApplication.kt` (lines 176-179)

```kotlin
try {
    val validTitle = requireNotNull(item.title.takeIf { it.isNotBlank() }) {
        "Item ${item.id} has invalid title"
    }
    println("│   Item #${item.id}: '$validTitle' - valid")
}
```

**Location**: `repository/LibraryRepository.kt` (line 110)

```kotlin
checkNotNull(item) { "Item cannot be null" }
```

**Purpose**: 
- `requireNotNull` — throw `IllegalArgumentException` if null (precondition check)
- `checkNotNull` — throw `IllegalStateException` if null (state check)

---

## 📊 Application Flow

The application demonstrates all required features through the following scenarios:

### **Main Execution Flow** (`LibraryApplication.kt`)

1. **Initialize Repository** — creates singleton repository and resets ID generator
2. **Load Data** — uses companion object factory methods to create sample data
3. **Scenario A**: Groups items by publication year
4. **Scenario B**: Finds books with missing optional fields (ISBN)
5. **Scenario C**: Sorts books by publication year
6. **Scenario D**: Counts items by type
7. **Bonus**: Author statistics aggregation
8. **Validation**: Demonstrates `requireNotNull` and validation logic
9. **Sealed Class Demo**: Shows parsing with `ParseResult` sealed class hierarchy
10. **Final Report**: Displays comprehensive statistics

---

## ✅ Requirements Checklist

### 1. Domain Model (Open & Special Classes) ✓
- ✅ **Open class**: `LibraryItem` (base class with open methods)
- ✅ **Subclasses**: `Book`, `Magazine` extend `LibraryItem`
- ✅ **Data classes**: 
  - `Book` — book information
  - `Magazine` — magazine information
  - `ItemStatistics` — statistics report
  - `YearGroup` — year grouping
  - `AuthorStats` — author statistics
- ✅ **Sealed class**: 
  - `ParseResult` — type-safe parsing results
  - `SearchResult` — type-safe search results
- ✅ **Enum class**: `ItemCategory` — book/magazine categories

### 2. Objects ✓
- ✅ **Singleton objects**: 
  - `IdGenerator` — centralized ID generation
  - `LibraryConfig` — application configuration and validation
- ✅ **Companion objects**: 
  - `Book.Companion` — factory methods (fromCsv, createSample, createSamples)
  - `Magazine.Companion` — factory methods (createMonthly, createQuarterly, fromCsv)
  - `ItemCategory.Companion` — fromString converter

### 3. Properties ✓
- ✅ **Computed properties (custom getters)**: 
  - `LibraryItem.age` — calculated from current year
  - `Book.readingTime` — estimated from page count
  - `Book.displayTitle` — combines title + subtitle
  - `Book.formattedIsbn` — formatted ISBN display
  - `Book.authorInfo` — formatted author info
  - `Magazine.displayIssue` — formatted issue number
  - `Magazine.fullTitle` — combines publisher + title
  - `Magazine.publicationDate` — formatted publication date
- ✅ **Nullable properties**: `isbn?`, `subtitle?`, `publisher?`, `month?`
- ✅ **Property validation**: in init blocks with meaningful error messages

### 4. Null Safety ✓
- ✅ **Nullable types (`?`)**: `String?`, used in 4+ properties
- ✅ **Safe calls (`?.`)**: 20+ usages throughout codebase
- ✅ **Elvis operator (`?:`)**: 15+ usages for default values
- ✅ **`let` function**: 10+ usages for nullable processing
- ✅ **`requireNotNull`**: 2 usages in repository and main
- ✅ **`checkNotNull`**: 1 usage in validation logic

---

## 🎓 Key Learning Outcomes

This project demonstrates:

1. **Object-oriented design** with inheritance and polymorphism
2. **Idiomatic Kotlin** null-safety patterns
3. **Type safety** with sealed classes and exhaustive when expressions
4. **Property abstraction** with custom getters for computed values
5. **Singleton pattern** for shared application services
6. **Factory pattern** using companion objects
7. **Data modeling** with data classes for immutable DTOs
8. **Defensive programming** with validation and require/check functions

---

## 📦 Sample Output

When you run the application, you'll see:

```
═══════════════════════════════════════════════════════════
   LIBRARY CATALOG & ANALYZER
   Kotlin Core Language Features Demonstration
═══════════════════════════════════════════════════════════

┌─ STEP 1: Initializing Repository ─────────────────┐
│ Repository created                                 │
│ IdGenerator reset                                  │
└────────────────────────────────────────────────────┘

┌─ STEP 2: Loading Data ─────────────────────────────┐
│ Creating sample book via companion object...
Added: The Kotlin Programming Language: A Modern Approach (2024) - 450 pages
│
│ Parsing books from CSV via companion object...
Added: Foundation (1951) - 255 pages
Added: Dune (1965) - 412 pages
Added: 1984 (1949) - 328 pages
Added: Clean Code: A Handbook of Agile Software Craftsmanship (2008) - 464 pages
│
│ Creating magazines via companion methods...
Added: SciPress - Science Quarterly, Issue #4 (October-December 2024)
Added: National Geographic Society - National Geographic, Issue #3 (March 2024)
Added: TechMedia - Tech Monthly, Issue #15 (March 2024)
└────────────────────────────────────────────────────┘

┌─ SCENARIO A: Items Grouped by Publication Year ────┐
...
└────────────────────────────────────────────────────┘

┌─ SCENARIO B: Books with Missing Optional Fields ───┐
...
└────────────────────────────────────────────────────┘

┌─ SCENARIO C: Books Sorted by Publication Year ─────┐
...
└────────────────────────────────────────────────────┘

┌─ SCENARIO D: Item Count by Type ───────────────────┐
...
└────────────────────────────────────────────────────┘

┌─ BONUS: Author Statistics ─────────────────────────┐
...
└────────────────────────────────────────────────────┘

┌─ VALIDATION: Checking Data Integrity ──────────────┐
...
└────────────────────────────────────────────────────┘

┌─ SEALED CLASS Demo: Parsing Results ───────────────┐
...
└────────────────────────────────────────────────────┘

┌─ FINAL STATISTICS REPORT ──────────────────────────┐
╔════════════════════════════════════════╗
║     Library Statistics Report          ║
╠════════════════════════════════════════╣
║ Total Items:           8               ║
║ Books:                 5               ║
║ Magazines:             3               ║
...
╚════════════════════════════════════════╝
└────────────────────────────────────────┘
```

---

## 🧪 Testing the Features

### Test Null Safety
The application handles missing optional data gracefully:
- Books without ISBN are tracked
- Subtitles default to title-only display
- Magazine publishers/months use fallback formatting

### Test Sealed Classes
Try modifying `testCsvLines` in `LibraryApplication.kt` (line 206) to see how `ParseResult` handles:
- Valid input → `ParseResult.Success`
- Invalid input → `ParseResult.Error`
- Empty input → `ParseResult.EmptyInput`

### Test Companion Object Factories
Modify CSV data or use companion factory methods:
```kotlin
val book = Book.createSample()
val magazine = Magazine.createMonthly("Wired", 2024, 5)
```

---

## 📚 Code Tour

### Where to Find Each Feature

| Feature | File | Lines | Description |
|---------|------|-------|-------------|
| **Object (Singleton)** | `IdGenerator.kt` | 3-15 | ID generation service |
| **Object (Singleton)** | `LibraryConfig.kt` | 3-37 | Configuration constants |
| **Companion Object** | `Book.kt` | 43-96 | Factory methods |
| **Companion Object** | `Magazine.kt` | 37-121 | Factory methods |
| **Open Class** | `LibraryItem.kt` | 5-28 | Base class |
| **Data Class** | `Book.kt` | 6-14 | Extends open class |
| **Data Class** | `Magazine.kt` | 6-14 | Extends open class |
| **Data Class** | `ItemStatistics.kt` | 3-24 | Statistics DTO |
| **Sealed Class** | `ParseResult.kt` | 3-13 | Parse results |
| **Enum Class** | `ItemCategory.kt` | 3-19 | Item categories |
| **Computed Property** | `LibraryItem.kt` | 10-11 | `age` getter |
| **Computed Properties** | `Book.kt` | 18-28 | Multiple getters |
| **Nullable Properties** | `Book.kt` | 12-13 | `isbn?`, `subtitle?` |
| **Safe Calls** | `Book.kt` | 21-25 | `?.let`, `?:` |
| **Elvis Operator** | `Magazine.kt` | 21-25 | Default values |
| **`let` usage** | `Book.kt`, `LibraryApplication.kt` | Various | Nullable chains |
| **`requireNotNull`** | `LibraryRepository.kt` | 27-30 | Precondition |
| **`checkNotNull`** | `LibraryRepository.kt` | 110 | State check |

---

## 🏆 Assignment Compliance

This project fulfills all assignment requirements:

### ✅ 1. Domain Model
- Open class `LibraryItem` with two subclasses
- Two data classes (`Book`, `Magazine`)
- One sealed class (`ParseResult`)
- One enum class (`ItemCategory`)

### ✅ 2. Objects
- Two singleton objects (`IdGenerator`, `LibraryConfig`)
- Companion objects with factory methods in `Book` and `Magazine`

### ✅ 3. Properties
- 8+ computed properties with custom getters
- Nullable properties with formatted getters
- Properties solving real domain problems (not artificial)

### ✅ 4. Null Safety
- Nullable types declared and used extensively
- Safe calls (`?.`) in 20+ locations
- Elvis operator (`?:`) for defaults
- `let` for null-safe transformations
- `requireNotNull` and `checkNotNull` for validation

### ✅ 5. Application Flow
- Creates dataset using companion factory methods
- Stores in in-memory repository
- Executes 4+ analysis scenarios
- Prints readable formatted reports

---

## 💡 Design Decisions

### Why Open Class Instead of Interface?
`LibraryItem` is an open class (not interface) to:
- Share common implementation (`age` property, validation logic)
- Enforce constructor requirements
- Provide default implementations that can be overridden

### Why Data Classes for Book/Magazine?
Despite extending an open class, `Book` and `Magazine` are data classes because:
- They primarily hold data
- Structural equality is useful (comparing books by content)
- Auto-generated methods (`copy`, `toString`) are beneficial

### Why Sealed Class for ParseResult?
- Exhaustive when expressions at compile time
- Type-safe error handling
- Clear API: success, error, or empty input

### Why Object for IdGenerator/LibraryConfig?
- Stateful singleton (IdGenerator)
- Configuration constants (LibraryConfig)
- No need for multiple instances

---

## 🔧 Future Enhancements

Potential improvements:
- Add persistence layer (file I/O)
- Implement search with `SearchResult` sealed class
- Add more item types (DVD, AudioBook)
- Create ISBN validation logic
- Add unit tests

---

## 👨‍💻 Author

Created as part of **Kotlin Core Language Features** learning assignment.

**Time invested**: ~6-8 hours (reading + implementation)

**Focus Areas**:
- Objects and companion objects
- Open and special classes (data, sealed, enum)
- Properties with custom getters
- Comprehensive null safety patterns

---

## 📚 References

- [Kotlin Tour - Objects](https://kotlinlang.org/docs/kotlin-tour-intermediate-objects.html)
- [Kotlin Documentation - Classes](https://kotlinlang.org/docs/classes.html)
- [Kotlin Documentation - Null Safety](https://kotlinlang.org/docs/null-safety.html)
- [Kotlin Documentation - Properties](https://kotlinlang.org/docs/properties.html)

