package de.dhbw.campusmarket.data

import androidx.room.Database
import androidx.room.RoomDatabase

// Describes the database: which tables exist and in which version.
// exportSchema = false because we plan no migrations and need no schema folder.
@Database(entities = [Ad::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adDao(): AdDao
}
