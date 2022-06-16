package com.kjk.marsrealestate.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kjk.marsrealestate.database.MarsPropertyDatabase
import com.kjk.marsrealestate.database.MarsPropertyEntity
import com.kjk.marsrealestate.database.asDomainModel
import com.kjk.marsrealestate.domain.MarsProperty
import com.kjk.marsrealestate.network.MarsApi
import com.kjk.marsrealestate.network.asDatabaseEntity
import com.kjk.marsrealestate.overview.MarsApiFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 *  Repository pattern 추가.
 *
 */
class MarsRepository(
    private val database: MarsPropertyDatabase
) {

    /**
     *  database로 부터 fetch한 data
     */
    private val marsPropertyEntities: LiveData<List<MarsPropertyEntity>> = database.databaseDao.getAllMarsProperties()


    /**
     * database object를 domain object로 변환하는 LiveData
     */
    val marsProperties: LiveData<List<MarsProperty>> = Transformations.map(marsPropertyEntities) {
        it.asDomainModel()
    }

    /**
     *  network의 data와 local data의 sync를 맟춰야한다.
     */
    suspend fun refresh(filter: MarsApiFilter) {
        withContext(Dispatchers.IO) {
            // 1. network로 부터 fetch
            val marsProperties = MarsApi.retrofitService.getProperties(filter.value)

            // 2. local database에 저장
            database.databaseDao.insertAll(marsProperties.asDatabaseEntity())
        }
    }
}