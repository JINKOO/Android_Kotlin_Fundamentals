package com.kjk.devbytes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoDatabaseDao {
    /**
     * local 저장소(Room)에 있는 최신화된 video data를 fetch하도록 제공.
     * LiveData로 반환해야, database가 변경 사항에 대해, 반영해서 최신의 상태를 유지하고,
     * 변경이 발생했을 때, UI Controller에서도 변경이 적용된 data를 fetch해 사용할 수 있다.
     */
    @Query("SELECT * FROM database_video")
    fun getAllVideos(): LiveData<List<VideoDatabaseEntity>>

    /**
     * Network로 부터 fetch한 data들을 database에 저장한다.
     * 이는 network의 data와, local 저장소의 sync를 맞추기 위해
     * 항상 최신의 data를 유지해야하기 때문에, network로 부터 update된 data를 fetch하면,
     * 기존의 data를 덮어 씌우는 방식을 사용한다.
     * OnConflictStrategy = REPLACE
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(videos: List<VideoDatabaseEntity>)
}