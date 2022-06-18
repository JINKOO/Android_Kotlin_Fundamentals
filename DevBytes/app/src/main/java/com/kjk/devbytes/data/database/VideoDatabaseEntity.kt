package com.kjk.devbytes.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kjk.devbytes.data.domain.DevByteVideo

/** database DTO 이다.*/
@Entity(tableName = "database_video")
data class VideoDatabaseEntity (
    // 여기서는 모든 video가 고유의 url을 가진다고 가정한다.
    @PrimaryKey
    val url: String,
    val updated: String,
    val title: String,
    val description: String,
    val thumbnail: String
)

/** database object를 domain object로 변환한다. */
fun List<VideoDatabaseEntity>.asDomainModel(): List<DevByteVideo> {
    return map {
        DevByteVideo(
            title = it.title,
            description = it.description,
            url = it.url,
            thumbnail = it.thumbnail,
            updated = it.updated
        )
    }
}