package com.kjk.marsrealestate.overview

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kjk.marsrealestate.R

@BindingAdapter("imageSrc")
fun setImageSrc(imageView: ImageView, imgSrcUrl: String?) {
    imgSrcUrl?.let {
        val imgUrl = imgSrcUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imgUrl)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.ic_broken_image)
            )
            .into(imageView)
    }
}