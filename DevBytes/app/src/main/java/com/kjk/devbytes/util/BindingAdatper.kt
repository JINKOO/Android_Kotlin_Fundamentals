package com.kjk.devbytes.util

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kjk.devbytes.R

@BindingAdapter("imgSrc")
fun setThumbnailImage(imageView: ImageView, imgSrcUrl: String?) {
    imgSrcUrl?.let {

        val imgSrc = imgSrcUrl.toUri().buildUpon().scheme("https").build()

        // Glide 라이브러리를 사용해서 이미지를 set해야함!!!!!!!!
        Glide.with(imageView.context)
            .load(imgSrc)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.ic_broken_image))
            .into(imageView)
    }
}