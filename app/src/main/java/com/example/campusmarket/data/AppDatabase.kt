package com.example.campusmarket.data

import androidx.room.Database
import androidx.room.RoomDatabase

// Beschreibt die Datenbank: welche Tabellen es gibt und in welcher Version.
// exportSchema = false, weil wir keine Migrationen planen und keinen Schema-Ordner brauchen.
@Database(entities = [Ad::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adDao(): AdDao
}
