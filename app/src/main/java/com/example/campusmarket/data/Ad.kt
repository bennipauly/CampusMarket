package com.example.campusmarket.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Eine Anzeige. Jede Instanz ist eine Zeile in der Tabelle "ads".
@Entity(tableName = "ads")
data class Ad(
    // id = 0 bedeutet "neu"; Room vergibt beim Einfügen automatisch die nächste freie id.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val price: Double,
    val category: Category,
    val condition: Condition,
    // Übergabeort, z. B. "Bibliothek".
    val location: String,
    // Pfad bzw. Uri des Fotos; null, wenn es kein Foto gibt.
    val imageUri: String? = null,
    val isFavorite: Boolean = false,
    val isSold: Boolean = false,
    // Erstellzeitpunkt in Millisekunden seit 1970, wird später für die Sortierung gebraucht.
    val createdAt: Long
)
