package com.kjk.marsrealestate.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kjk.marsrealestate.domain.MarsProperty

@Entity(tableName = "mars_property_database")
data class MarsPropertyEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val imgSrcUrl: String,
    val price: Double
)

fun List<MarsPropertyEntity>.asDomainModel(): List<MarsProperty> {
    return map {
        MarsProperty(
            id = it.id,
            price = it.price,
            type = it.type,
            imgSrcUrl = it.imgSrcUrl
        )
    }
}