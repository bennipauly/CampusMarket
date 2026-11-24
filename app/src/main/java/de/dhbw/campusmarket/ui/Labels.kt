package de.dhbw.campusmarket.ui

import androidx.annotation.StringRes
import de.dhbw.campusmarket.R
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition
import de.dhbw.campusmarket.logic.SortOrder

// Maps every enum value to its German text in strings.xml.
// This way the data and logic layers know no Android resources, and a "when" without else
// makes the compiler complain if a new enum value has no text.

@StringRes
fun Category.labelRes(): Int = when (this) {
    Category.BOOKS -> R.string.category_books
    Category.SCRIPTS -> R.string.category_scripts
    Category.CALCULATORS -> R.string.category_calculators
    Category.TECH -> R.string.category_tech
    Category.FURNITURE -> R.string.category_furniture
    Category.OTHER -> R.string.category_other
}

@StringRes
fun Condition.labelRes(): Int = when (this) {
    Condition.NEW -> R.string.condition_new
    Condition.LIKE_NEW -> R.string.condition_like_new
    Condition.GOOD -> R.string.condition_good
    Condition.USED -> R.string.condition_used
}

@StringRes
fun SortOrder.labelRes(): Int = when (this) {
    SortOrder.NEWEST -> R.string.sort_newest
    SortOrder.PRICE_ASCENDING -> R.string.sort_price_ascending
    SortOrder.PRICE_DESCENDING -> R.string.sort_price_descending
    SortOrder.TITLE -> R.string.sort_title
}
