package com.kjk.marsrealestate.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarsProperty(
    val id: String,
    val price: Double,
    val type: String,
    @Json(name = "img_src")
    val imgSrcUrl: String
) : Parcelable {
    /** Rent인지 Buy인지 판별하는 flag, 해당 flag에 따라, 달러 이미지를 표시한다.*/
    val isRental: Boolean
        get() = type == "rent"
}