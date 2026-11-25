package de.dhbw.campusmarket.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// An ad. Every instance is one row in the table "ads".
// The unique index on remoteId makes sure every ad from the test data exists only once.
@Entity(tableName = "ads", indices = [Index(value = ["remoteId"], unique = true)])
data class Ad(
    // id = 0 means "new"; Room assigns the next free id automatically on insert.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    // Id from testdata/anzeigen.json; null for ads that were created in the app.
    val remoteId: Long? = null,
    val title: String,
    val description: String,
    val price: Double,
    val category: Category,
    val condition: Condition,
    // Handover location, e.g. "Bibliothek".
    val location: String,
    // Uri of the photo: content:// for own photos, https:// for test data; null if there is no photo.
    val imageUri: String? = null,
    val isFavorite: Boolean = false,
    val isSold: Boolean = false,
    // Creation time in milliseconds since 1970, used for sorting.
    val createdAt: Long
)
