package com.kjk.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 보통 tableName argmument는 사용하는 것을 추천한다.
@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight(
    @PrimaryKey(autoGenerate = true)
    val nightId: Long = 0L,

    @ColumnInfo(name = "start_time_millis")
    var startTimeMillis: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time_millis")
    var endTimeMillis: Long = startTimeMillis,

    @ColumnInfo(name = "quality_rating")
    var sleepQuality: Int = -1
)
