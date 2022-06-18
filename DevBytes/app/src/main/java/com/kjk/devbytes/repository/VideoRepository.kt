package com.kjk.devbytes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kjk.devbytes.data.database.VideoDatabase
import com.kjk.devbytes.data.database.asDomainModel
import com.kjk.devbytes.data.domain.DevByteVideo
import com.kjk.devbytes.data.network.VideoApi
import com.kjk.devbytes.data.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * data sources와, 다른 app 구성요소와 분리 시키는 컴포넌트.
 * 여기서는 Room을 사용해서, network로 얻은 data를 cache한다.
 */
class VideoRepository(
    private val database: VideoDatabase
) {

    /**
     * Transformation을 사용해, database로 부터 fetch한 data를 domain object로 변경한다.
     */
    val videos: LiveData<List<DevByteVideo>> = Transformations.map(database.videoDatabaseDao.getAllVideos()) {
        it.asDomainModel()
    }

    /**
     * network로 부터 data를 fetch하고,
     * database (Room)에 저장한다.
     */
    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            // 1. network로 부터 data fetch
            val videosFromRemote = VideoApi.retrofitService.getVideos()

            // 2. Room database에 저장
            database.videoDatabaseDao.insertAll(videosFromRemote.asDatabaseModel())
        }
    }
}