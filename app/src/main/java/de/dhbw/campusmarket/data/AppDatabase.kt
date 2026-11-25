package de.dhbw.campusmarket.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Describes the database: which tables exist and in which version.
// exportSchema = false: the only migration is written by hand below, no schema folder is needed.
@Database(entities = [Ad::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adDao(): AdDao
}

// Version 1 -> 2: new column remoteId for the ads from the network.
// Existing ads (own ads and old sample ads) keep all their data and get remoteId = null.
// The index name must be exactly the one Room generates for @Index on "remoteId".
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE ads ADD COLUMN remoteId INTEGER")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_ads_remoteId ON ads (remoteId)")
    }
}
