package com.kjk.devbytes.util

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kjk.devbytes.R
import com.kjk.devbytes.domain.DevByteVideo
import com.kjk.devbytes.viewModel.VideoApiStatus

/** RecyclerView Item에 image set */
@BindingAdapter("imgSrc")
fun setThumbnailImage(imageView: ImageView, imgSrcUrl: String?) {
    imgSrcUrl?.let {

        val imgSrc = imgSrcUrl.toUri().buildUpon().scheme("https").build()

        // Glide 라이브러리를 사용해서 이미지를 set해야함!!!!!!!!
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

/** RecyclerView Item에 Progressbar set */
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
                    Log.d("DevByte", "hideProgressBar: ")
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}

/** connection error */
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