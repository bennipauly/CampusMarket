package de.dhbw.campusmarket.data.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

// The public GitHub repository of this project, read through raw.githubusercontent.com.
// The branch is "master"; with "main" every request would end in 404.
const val BASE_URL = "https://raw.githubusercontent.com/bennipauly/CampusMarket/master/"

// Retrofit needs an interface and generates the implementation itself at runtime.
// This is the only interface in the project; CLAUDE.md allows it because Retrofit requires it.
interface AdApi {

    // GET BASE_URL + "testdata/anzeigen.json". Retrofit runs the request on its own thread,
    // so the suspend function does not block the main thread.
    // Errors: IOException (no connection), HttpException (e.g. 404), SerializationException (bad JSON).
    @GET("testdata/anzeigen.json")
    suspend fun getAds(): List<AdDto>
}

// JSON settings shared by Retrofit and the JUnit tests.
// ignoreUnknownKeys: additional fields in the file are ignored instead of failing.
val adJson = Json { ignoreUnknownKeys = true }

// Builds the Retrofit client. Called exactly once in CampusMarketApplication.
fun createAdApi(): AdApi {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(adJson.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(AdApi::class.java)
}
