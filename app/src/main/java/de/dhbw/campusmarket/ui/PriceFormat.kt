package de.dhbw.campusmarket.ui

import java.util.Locale

// Preis immer im deutschen Format ("12,50 €"), egal auf welche Sprache das Gerät
// eingestellt ist. Die App ist nur auf Deutsch, also soll auch der Preis so aussehen.
fun formatPrice(price: Double): String = String.format(Locale.GERMANY, "%.2f €", price)
