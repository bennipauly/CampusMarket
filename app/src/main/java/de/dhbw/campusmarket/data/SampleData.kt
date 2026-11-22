package de.dhbw.campusmarket.data

private const val ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L

// Beispielanzeigen für den ersten Start. "now" wird übergeben, damit die Anzeigen
// unterschiedlich alt sind (heute, gestern, vor zwei Tagen, ...).
fun sampleAds(now: Long): List<Ad> = listOf(
    Ad(
        title = "Analysis 1 – Lehrbuch",
        description = "Kaum benutzt, ein paar Markierungen mit Bleistift.",
        price = 15.0,
        category = Category.BOOKS,
        condition = Condition.LIKE_NEW,
        location = "Bibliothek",
        createdAt = now
    ),
    Ad(
        title = "Skript Theoretische Informatik",
        description = "Ausgedrucktes Skript mit eigenen Notizen, gebunden.",
        price = 5.0,
        category = Category.SCRIPTS,
        condition = Condition.GOOD,
        location = "Mensa",
        createdAt = now - ONE_DAY_MILLIS
    ),
    Ad(
        title = "Taschenrechner TI-30X",
        description = "Funktioniert einwandfrei, für Klausuren zugelassen.",
        price = 12.5,
        category = Category.CALCULATORS,
        condition = Condition.GOOD,
        location = "Haupteingang",
        createdAt = now - 2 * ONE_DAY_MILLIS
    ),
    Ad(
        title = "USB-C-Hub mit HDMI",
        description = "4 Anschlüsse, Originalverpackung vorhanden.",
        price = 20.0,
        category = Category.TECH,
        condition = Condition.NEW,
        location = "Cafeteria",
        createdAt = now - 3 * ONE_DAY_MILLIS
    ),
    Ad(
        title = "Schreibtischstuhl",
        description = "Höhenverstellbar, Stoff etwas abgenutzt. Nur Abholung.",
        price = 25.0,
        category = Category.FURNITURE,
        condition = Condition.USED,
        location = "Parkplatz",
        createdAt = now - 4 * ONE_DAY_MILLIS
    ),
    Ad(
        title = "Programmieren in Kotlin",
        description = "Neuwertig, nur einmal durchgeblättert.",
        price = 18.0,
        category = Category.BOOKS,
        condition = Condition.NEW,
        location = "Bibliothek",
        isSold = true,
        createdAt = now - 5 * ONE_DAY_MILLIS
    )
)
