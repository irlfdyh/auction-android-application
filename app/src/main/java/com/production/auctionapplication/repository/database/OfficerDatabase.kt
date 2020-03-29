package com.production.auctionapplication.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import timber.log.Timber

@Database(entities = [OfficerAccountData::class], version = 2, exportSchema = false)
abstract class OfficerDatabase : RoomDatabase() {

    abstract val officerDatabaseDao: OfficerDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: OfficerDatabase? = null

        fun getInstance(context: Context): OfficerDatabase {
            Timber.i("Creating Database")
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        OfficerDatabase::class.java,
                        "officer_preference_database.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                    Timber.i("Database Created")
                }
                return instance
            }
        }
    }

}

