package com.example.movies.utils

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImage")
fun bindingImage(image: ImageView, imageUrl: String?) {
    Glide.with(image.context)
        .load(imageUrl)
        .fitCenter()
        .into(image)
}