package de.dhbw.campusmarket

import android.app.Application
import android.util.Log
import androidx.room.Room
import de.dhbw.campusmarket.data.AdRepository
import de.dhbw.campusmarket.data.AppDatabase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private const val TAG = "CampusMarketApplication"

// Created by Android exactly once per app process (see android:name in the manifest).
// That is why database and repository are created here exactly once and all ViewModels share them.
class CampusMarketApplication : Application() {

    // "by lazy": built on first access and reused afterwards.
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "campusmarket.db").build()
    }

    val repository: AdRepository by lazy {
        AdRepository(database.adDao())
    }

    override fun onCreate() {
        super.onCreate()
        // Room runs suspend functions in the background itself, so the main thread does not block.
        MainScope().launch {
            try {
                repository.insertSampleDataIfEmpty()
            } catch (e: Exception) {
                // Without sample data the app still works, the list is just empty.
                Log.e(TAG, "Inserting the sample data failed", e)
            }
        }
    }
}
