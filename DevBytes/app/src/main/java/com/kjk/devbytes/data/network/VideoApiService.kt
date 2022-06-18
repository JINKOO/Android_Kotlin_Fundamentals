package com.kjk.devbytes.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/** retrofit api call을 위한 base url */
private val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface VideoApiService {

    /** coroutine을 사용해서 api call */
    @GET("devbytes")
    suspend fun getVideos(): NetworkVideoContainer
}

/** singleton으로 ServiceApi를 생성한다. */
object VideoApi {
    val retrofitService: VideoApiService by lazy {
        retrofit.create(VideoApiService::class.java)
    }
}