package de.dhbw.campusmarket.ui.navigation

import kotlinx.serialization.Serializable

// Navigation destinations. @Serializable makes them type-safe: Navigation Compose
// turns them into a route itself, so there are no typos in route strings.

// Start screen with all ads. Needs no parameters, so it is an object.
@Serializable
object AdList

// Detail screen. Only the id is passed; the screen loads the ad itself from the database.
@Serializable
data class AdDetail(val adId: Long)

// Photo of an ad in full screen. Again only the id; the screen gets the photo Uri itself.
@Serializable
data class PhotoViewer(val adId: Long)

// Form for creating a new ad.
@Serializable
object AdForm

// The same form for editing an existing ad. Only the id is passed.
@Serializable
data class AdEdit(val adId: Long)
