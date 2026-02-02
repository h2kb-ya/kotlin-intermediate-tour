# Kotlin Assignment — Intermediate Concepts Project
## Domain: **Books and Library Items**

---

## 🎯 Objective

Continue learning Kotlin by studying selected intermediate topics and applying them in a practical mini-project.  
The project is based on the domain **“Books and Library Items.”**

You must:

- Read selected Kotlin Tour sections
- Implement a console application
- Demonstrate extension functions, scope functions, lambda expressions with receiver, and classes/interfaces

Estimated effort: **6–8 hours** (reading + coding)

---

## 📘 Required Reading (Kotlin Tour)

You must read and understand the following chapters:

1. **Extension functions**
2. **Scope functions** (`let`, `run`, `apply`, `also`, `with`)
3. **Lambda expressions with receiver**
4. **Classes and interfaces**

(Optional: Objects, open classes, special classes.)

---

# 📚 Project Theme
## **Library Data Processing Toolkit**

You will build a Kotlin application that simulates a simple “Library Processing Toolkit.”  
It will model different types of library items and provide operations to transform, filter, analyze, and build them using idiomatic Kotlin features.

---

# 📦 Project Requirements

## 1. Domain Model (Classes & Interfaces)

Define the following:

### **Interface**
- `LibraryItem` — common interface for all items.

### **Implementing Classes**
At least **two** concrete classes, for example:

- `Book(title: String, author: String, year: Int, pages: Int)`
- `Magazine(title: String, issue: Int, year: Int)`

You may add more types if useful.

### **Service Class**
Create a class such as:

- `LibraryRepository`  
  or
- `LibraryService`

This class manages a collection of items.

---

## 2. Extension Functions

Create **at least three** extension functions related to the domain.

Examples:

- `Book.readingTime(minutesPerPage: Int): Int`
- `List<Book>.filterByAuthor(author: String)`
- `List<LibraryItem>.publishedAfter(year: Int)`
- `LibraryItem.displayInfo(): String`

At least one extension must operate on a collection.

---

## 3. Scope Functions

Use **three different scope functions** meaningfully:

- `let`
- `run`
- `apply`
- `also`
- `with`

Examples:

- Configure a `Book` using `apply`
- Process optional values using `let`
- Add logging using `also`
- Build aggregated text using `run`

Usage must be idiomatic, not artificial.

---

## 4. Lambda Expressions with Receiver (Custom DSL)

Create a mini “builder style DSL” for constructing library items.

Example:

```kotlin
val dune = book {
    title = "Dune"
    author = "Frank Herbert"
    year = 1965
    pages = 412
}
```

or:
```kotlin
val scienceMag = magazine {
title = "Science Weekly"
issue = 42
year = 2023
}
```

This demonstrates understanding of DSL-like Kotlin patterns.

## 5. Main Application

Your main() function must:

- Create several books and magazines
- Store them in the repository
- Apply extension functions for processing
- Use scope functions in meaningful places
- Use your custom DSL to create at least one item
-Print results to the console in a readable format

## 📄 Deliverables
A runnable Kotlin program.