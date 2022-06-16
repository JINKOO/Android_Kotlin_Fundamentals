package com.kjk.devbytes.domain

import android.net.Uri

/**
 * Domain object
 * 실제 UI data에 보여줄 data 이다.*/
data class DevByteVideo(
    val title: String,
    val description: String,
    val url: String,
    val updated: String,
    val thumbnail: String
) {
    val launchUri: Uri
        get() {
            val httpUri = Uri.parse(url)
            return Uri.parse("vnd.youtube:" + httpUri.getQueryParameter("v"))
        }
}