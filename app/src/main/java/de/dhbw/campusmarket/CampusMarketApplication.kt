package de.dhbw.campusmarket

import android.app.Application
import androidx.room.Room
import de.dhbw.campusmarket.data.AdRepository
import de.dhbw.campusmarket.data.AppDatabase
import de.dhbw.campusmarket.data.MIGRATION_1_2
import de.dhbw.campusmarket.data.network.ConnectionChecker
import de.dhbw.campusmarket.data.network.createAdApi

// Created by Android exactly once per app process (see android:name in the manifest).
// That is why database, network client and repository are created here exactly once
// and all ViewModels share them.
class CampusMarketApplication : Application() {

    // "by lazy": built on first access and reused afterwards.
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "campusmarket.db")
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    val repository: AdRepository by lazy {
        AdRepository(database.adDao(), createAdApi(), ConnectionChecker(this))
    }
}
