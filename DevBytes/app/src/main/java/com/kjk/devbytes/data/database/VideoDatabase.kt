package com.kjk.devbytes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [VideoDatabaseEntity::class], version = 1)
abstract class VideoDatabase : RoomDatabase() {

    abstract val videoDatabaseDao: VideoDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: VideoDatabase? = null

        fun getInstance(context: Context): VideoDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        VideoDatabase::class.java,
                        "database_video"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}