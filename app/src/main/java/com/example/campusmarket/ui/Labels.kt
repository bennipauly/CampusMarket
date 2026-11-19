package com.example.campusmarket.ui

import androidx.annotation.StringRes
import com.example.campusmarket.R
import com.example.campusmarket.data.Category
import com.example.campusmarket.data.Condition

// Ordnet jedem Enum-Wert seinen deutschen Text aus strings.xml zu.
// Die Datenschicht kennt so keine Android-Ressourcen, und "when" ohne else
// sorgt dafür, dass der Compiler meckert, wenn ein neuer Enum-Wert keinen Text hat.

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
