package de.dhbw.campusmarket.ui

import java.util.Locale

// Price always in German format ("12,50 €"), whatever language the device
// is set to. The app is only in German, so the price should look German too.
fun formatPrice(price: Double): String = String.format(Locale.GERMANY, "%.2f €", price)
