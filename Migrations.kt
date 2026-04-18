package com.asma.cinetrack.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE favorite_movies ADD COLUMN voteAverage REAL NOT NULL DEFAULT 0.0")
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE favorite_movies ADD COLUMN voteAverage REAL NOT NULL DEFAULT 0.0")
    }
}
