package com.kjk.devbytes.util

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kjk.devbytes.R
import com.kjk.devbytes.domain.DevByteVideo
import com.kjk.devbytes.ui.VideoAdapter
import com.kjk.devbytes.viewModel.VideoApiStatus

/**
 * RecyclerView Item에 image set
 */
@BindingAdapter("imgSrc")
fun setThumbnailImage(imageView: ImageView, imgSrcUrl: String?) {
    imgSrcUrl?.let {
        val imgSrc = imgSrcUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imgSrc)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)
    }
}

/**
 * RecyclerView Item에 Progressbar set
 */
@BindingAdapter("apiStatus", "videoList")
fun hideProgressBar(
    progressBar: ProgressBar,
    apiStatus: VideoApiStatus?,
    videoList: List<DevByteVideo>?
) {
    // progresssbar를 set해야한다.
    if (videoList == null) {
        progressBar.visibility = View.GONE
    } else {
        apiStatus?.let {
            when (apiStatus) {
                VideoApiStatus.DONE -> {
                    progressBar.visibility = View.GONE
                }
                VideoApiStatus.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                VideoApiStatus.ERROR-> {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}

/**
 * network오류시 보여주는 이미지
 */
@BindingAdapter("errorImage")
fun setErrorImage(imageView: ImageView, apiStatus: VideoApiStatus?) {
    apiStatus?.let {
        when(apiStatus) {
            VideoApiStatus.ERROR -> {
                imageView.visibility = View.VISIBLE
            }
            VideoApiStatus.DONE,
            VideoApiStatus.LOADING -> {
                imageView.visibility = View.GONE
            }
        }
    }
}

/**
 * recyclerview에 binding adapter를 사용해서
 * data를 set한다.
 */
@BindingAdapter("videoList")
fun setVideoDataToList(recyclerView: RecyclerView, videos: List<DevByteVideo>?) {
    val adapter = recyclerView.adapter as VideoAdapter
    videos?.let {
        adapter.submitList(videos)
    }
}