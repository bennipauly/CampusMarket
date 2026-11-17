package com.example.campusmarket

import android.app.Application
import androidx.room.Room
import com.example.campusmarket.data.AdRepository
import com.example.campusmarket.data.AppDatabase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

// Wird von Android genau einmal pro App-Prozess erzeugt (siehe android:name im Manifest).
// Darum entstehen Datenbank und Repository hier genau einmal und alle ViewModels teilen sie.
class CampusMarketApplication : Application() {

    // "by lazy": wird erst beim ersten Zugriff gebaut und danach wiederverwendet.
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "campusmarket.db").build()
    }

    val repository: AdRepository by lazy {
        AdRepository(database.adDao())
    }

    override fun onCreate() {
        super.onCreate()
        // Room führt suspend-Funktionen selbst im Hintergrund aus, der Main-Thread blockiert also nicht.
        MainScope().launch {
            repository.insertSampleDataIfEmpty()
        }
    }
}
