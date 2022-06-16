package com.kjk.marsrealestate.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MarsPropertyEntity::class], version = 1)
abstract class MarsPropertyDatabase : RoomDatabase() {
    abstract val databaseDao: MarsPropertyDao

    companion object {
        private var INSTANCE: MarsPropertyDatabase? = null
        fun getInstance(context: Context): MarsPropertyDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        MarsPropertyDatabase::class.java,
                        "mars_property_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}