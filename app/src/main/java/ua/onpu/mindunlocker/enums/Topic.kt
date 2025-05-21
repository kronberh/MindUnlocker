package ua.onpu.mindunlocker.enums

enum class Topic(val displayName: String, val defaultOperations: Set<Char>) {
    ALGEBRA("Algebra", setOf('+', '-', '*', '/')),
    FRACTIONS("Fractions", setOf('+', '-', '/')),
    GEOMETRY("Geometry", setOf('+', '-'))
}
