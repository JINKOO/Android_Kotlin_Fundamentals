package com.kjk.marsrealestate.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MarsPropertyDao {

    /**
     * network로 부터 fetch해 database에 저장한 data를
     * fetch하는 함수.
     */
    @Query("SELECT * FROM mars_property_database")
    fun getAllMarsProperties(): LiveData<List<MarsPropertyEntity>>

    /**
     *  network로 부터 fetch한 data를 database에 저장한다.
     *  offline cache를 담당하는 함수.
     *  이미 network로 부터 fetch해 저장한 data가 있다면,
     *  다시 network로 부터 fetch해 새로 update된 값으로 덮어 씌우기 위해, REPLACE를 사용한다.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(marsProperties: List<MarsPropertyEntity>)
}