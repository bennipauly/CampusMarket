package de.dhbw.campusmarket.ui

import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition

private const val ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L

// Example ads that are only used by the @Preview functions in Android Studio.
// The app itself loads its ads from testdata/anzeigen.json (see AdRepository).
// The ids are set here because in the app Room assigns them on insert.
fun previewAds(): List<Ad> = listOf(
    Ad(
        title = "Analysis 1 – Lehrbuch",
        description = "Kaum benutzt, ein paar Markierungen mit Bleistift.",
        price = 15.0,
        category = Category.BOOKS,
        condition = Condition.LIKE_NEW,
        location = "Bibliothek",
        createdAt = 0L
    ),
    Ad(
        title = "Skript Theoretische Informatik",
        description = "Ausgedrucktes Skript mit eigenen Notizen, gebunden.",
        price = 5.0,
        category = Category.SCRIPTS,
        condition = Condition.GOOD,
        location = "Mensa",
        createdAt = -ONE_DAY_MILLIS
    ),
    Ad(
        title = "Taschenrechner TI-30X",
        description = "Funktioniert einwandfrei, für Klausuren zugelassen.",
        price = 12.5,
        category = Category.CALCULATORS,
        condition = Condition.GOOD,
        location = "Haupteingang",
        createdAt = -2 * ONE_DAY_MILLIS
    ),
    Ad(
        title = "USB-C-Hub mit HDMI",
        description = "4 Anschlüsse, Originalverpackung vorhanden.",
        price = 20.0,
        category = Category.TECH,
        condition = Condition.NEW,
        location = "Cafeteria",
        createdAt = -3 * ONE_DAY_MILLIS
    ),
    Ad(
        title = "Schreibtischstuhl",
        description = "Höhenverstellbar, Stoff etwas abgenutzt. Nur Abholung.",
        price = 25.0,
        category = Category.FURNITURE,
        condition = Condition.USED,
        location = "Parkplatz",
        createdAt = -4 * ONE_DAY_MILLIS
    ),
    Ad(
        title = "Programmieren in Kotlin",
        description = "Neuwertig, nur einmal durchgeblättert.",
        price = 18.0,
        category = Category.BOOKS,
        condition = Condition.NEW,
        location = "Bibliothek",
        isSold = true,
        createdAt = -5 * ONE_DAY_MILLIS
    )
).mapIndexed { index, ad -> ad.copy(id = index + 1L) }
