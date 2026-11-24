package de.dhbw.campusmarket.logic

import java.util.Locale

// Pure Kotlin functions for validating the form input, so they can be tested with JUnit.

const val MAX_TITLE_LENGTH = 60

// The title must not be empty (not even only spaces) and may have at most 60 characters.
fun isValidTitle(title: String): Boolean {
    val text = title.trim()
    return text.isNotEmpty() && text.length <= MAX_TITLE_LENGTH
}

fun isValidLocation(location: String): Boolean = location.isNotBlank()

// Allows 1 to 5 digits, optionally followed by a comma or dot with 1 to 2 decimal places.
// Examples: "5", "12,5", "12.50". A pattern instead of only toDoubleOrNull(), because toDoubleOrNull()
// would also accept "-5", "1e3" or "NaN".
private val PRICE_PATTERN = Regex("""\d{1,5}([.,]\d{1,2})?""")

// Converts the input into a price. Returns null if the input is invalid.
fun parsePrice(input: String): Double? {
    val text = input.trim()
    if (!PRICE_PATTERN.matches(text)) {
        return null
    }
    return text.replace(',', '.').toDouble()
}

// The other way round for editing: price from Room as text for the form, e.g. 12.5 -> "12,50".
// Locale.GERMANY makes sure of the comma, whatever language the device uses.
fun priceToInput(price: Double): String = String.format(Locale.GERMANY, "%.2f", price)
