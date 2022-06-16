package com.kjk.marsrealestate.overview

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kjk.marsrealestate.R
import com.kjk.marsrealestate.domain.MarsProperty

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

@BindingAdapter("recyclerViewData")
fun setRecyclerView(recyclerView: RecyclerView, properties: List<MarsProperty>?) {
    val adapter = recyclerView.adapter as MarsRealEstateAdapter
    properties?.let {
        adapter.submitList(properties)
    }
}

@BindingAdapter("marsApiStatus")
fun bindStatus(statusImageView: ImageView, status: MarsApiStatus?) {
    status?.let {
        when(status) {
            MarsApiStatus.LOADING -> {
                statusImageView.visibility = View.VISIBLE
                statusImageView.setImageResource(R.drawable.loading_img)
            }
            MarsApiStatus.DONE -> {
                statusImageView.visibility = View.GONE
            }
            MarsApiStatus.ERROR -> {
                statusImageView.visibility = View.VISIBLE
                statusImageView.setImageResource(R.drawable.ic_broken_image)
            }
        }
    }
}