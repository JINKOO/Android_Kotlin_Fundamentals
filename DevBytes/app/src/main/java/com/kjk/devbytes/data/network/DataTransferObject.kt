package com.kjk.devbytes.data.network

import com.kjk.devbytes.data.database.VideoDatabaseEntity
import com.kjk.devbytes.data.domain.DevByteVideo
import com.squareup.moshi.JsonClass


/** JsonClass는 JSON Object에 대응하는 역할을 한다.
 * generateAdapter라는 파라미터를 true로 해야 codegen 방식의 역직렬화, 직렬화가 가능하다. */
@JsonClass(generateAdapter = true)
data class NetworkVideoContainer(
    val videos: List<NetworkVideo>
)

/**
 * Network object
 * Network로 부터 받아온 network entity*/
@JsonClass(generateAdapter = true)
data class NetworkVideo(
    val title: String,
    val description: String,
    val url: String,
    val updated: String,
    val thumbnail: String,
    val closedCaptions: String?
)

/** network로 부터 받아온 entity를 domain entity로 변경*/
fun NetworkVideoContainer.asDomainModel(): List<DevByteVideo> {
    return videos.map {
        DevByteVideo(
            title = it.title,
            description = it.description,
            url = it.url,
            updated = it.updated,
            thumbnail = it.thumbnail
        )
    }
}

/** 추후에 network entity를 database entity로 변경해야 한다.*/
fun NetworkVideoContainer.asDatabaseModel(): List<VideoDatabaseEntity> {
    return videos.map {
        VideoDatabaseEntity(
            url = it.url,
            updated = it.updated,
            title = it.title,
            description = it.description,
            thumbnail = it.thumbnail
        )
    }
}