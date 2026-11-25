package de.dhbw.campusmarket.data.network

import de.dhbw.campusmarket.data.Ad

private const val ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L

// Pure Kotlin function without Android dependencies, so it can be tested with JUnit.
// Turns the ads from the JSON file into our Room model. The JSON has no date, so the first
// ad counts as created "now", the second one day earlier and so on. This keeps the file order
// when the list is sorted by "newest first".
fun toAds(dtos: List<AdDto>, now: Long): List<Ad> {
    return dtos.mapIndexed { index, dto ->
        Ad(
            remoteId = dto.remoteId,
            title = dto.title,
            description = dto.description,
            price = dto.price,
            category = dto.category,
            condition = dto.condition,
            location = dto.location,
            imageUri = dto.imageUrl,
            createdAt = now - index * ONE_DAY_MILLIS
        )
    }
}
