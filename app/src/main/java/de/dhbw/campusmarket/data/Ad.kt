package de.dhbw.campusmarket.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// An ad. Every instance is one row in the table "ads".
@Entity(tableName = "ads")
data class Ad(
    // id = 0 means "new"; Room assigns the next free id automatically on insert.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val price: Double,
    val category: Category,
    val condition: Condition,
    // Handover location, e.g. "Bibliothek".
    val location: String,
    // Path or Uri of the photo; null if there is no photo.
    val imageUri: String? = null,
    val isFavorite: Boolean = false,
    val isSold: Boolean = false,
    // Creation time in milliseconds since 1970, used for sorting.
    val createdAt: Long
)
