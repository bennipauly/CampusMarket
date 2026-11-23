package de.dhbw.campusmarket.logic

import java.util.Locale

// Reine Kotlin-Funktionen für die Prüfung der Formulareingaben, darum mit JUnit testbar.

const val MAX_TITLE_LENGTH = 60

// Titel darf nicht leer sein (auch nicht nur Leerzeichen) und höchstens 60 Zeichen haben.
fun isValidTitle(title: String): Boolean {
    val text = title.trim()
    return text.isNotEmpty() && text.length <= MAX_TITLE_LENGTH
}

fun isValidLocation(location: String): Boolean = location.isNotBlank()

// Erlaubt 1 bis 5 Ziffern, danach optional Komma oder Punkt mit 1 bis 2 Nachkommastellen.
// Beispiele: "5", "12,5", "12.50". Ein Muster statt nur toDoubleOrNull(), weil toDoubleOrNull()
// auch "-5", "1e3" oder "NaN" annehmen würde.
private val PRICE_PATTERN = Regex("""\d{1,5}([.,]\d{1,2})?""")

// Wandelt die Eingabe in einen Preis um. Gibt null zurück, wenn die Eingabe ungültig ist.
fun parsePrice(input: String): Double? {
    val text = input.trim()
    if (!PRICE_PATTERN.matches(text)) {
        return null
    }
    return text.replace(',', '.').toDouble()
}

// Umgekehrter Weg zum Bearbeiten: Preis aus Room als Text fürs Formular, z. B. 12.5 → "12,50".
// Locale.GERMANY sorgt für das Komma, egal welche Sprache das Gerät hat.
fun priceToInput(price: Double): String = String.format(Locale.GERMANY, "%.2f", price)
