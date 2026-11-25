package de.dhbw.campusmarket.data.network

import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// One ad exactly as it is written in testdata/anzeigen.json (DTO = data transfer object).
// The JSON keys are German, the Kotlin names English; @SerialName connects them.
// Category and Condition are read by their enum names (e.g. "BOOKS"). An unknown value or a
// missing field makes kotlinx-serialization throw a SerializationException.
@Serializable
data class AdDto(
    // Id of the ad in the JSON file. Stored as remoteId so the same ad is not inserted twice.
    @SerialName("id") val remoteId: Long,
    @SerialName("titel") val title: String,
    @SerialName("beschreibung") val description: String,
    @SerialName("preis") val price: Double,
    @SerialName("kategorie") val category: Category,
    @SerialName("zustand") val condition: Condition,
    @SerialName("ort") val location: String,
    // Full https URL of the image in the GitHub repository. Coil loads it directly.
    @SerialName("bildUrl") val imageUrl: String
)
