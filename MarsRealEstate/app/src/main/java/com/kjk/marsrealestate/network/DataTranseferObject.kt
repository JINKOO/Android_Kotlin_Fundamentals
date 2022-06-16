package com.kjk.marsrealestate.network

import com.kjk.marsrealestate.database.MarsPropertyEntity
import com.kjk.marsrealestate.domain.MarsProperty
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkMarsContainer(
    val marsProperties: List<NetworkMarsProperty>
)

@JsonClass(generateAdapter = true)
data class NetworkMarsProperty(
    val id: String,
    val type: String,
    @Json(name = "img_src")
    val imgSrcUrl: String,
    val price: Double
)

/**
 * network response object를 domain object로 변경한다.
 */
fun List<NetworkMarsProperty>.asDomainModel(): List<MarsProperty> {
    return map {
        MarsProperty(
            id = it.id,
            type = it.type,
            imgSrcUrl = it.imgSrcUrl,
            price = it.price
        )
    }
}

/**
 * network response object를 database entity로 변경한다.
 */
fun List<NetworkMarsProperty>.asDatabaseEntity(): List<MarsPropertyEntity> {
    return map {
        MarsPropertyEntity(
            id = it.id,
            type = it.type,
            imgSrcUrl = it.imgSrcUrl,
            price = it.price
        )
    }
}