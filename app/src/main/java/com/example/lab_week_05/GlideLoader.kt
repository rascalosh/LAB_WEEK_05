package com.example.lab_week_05

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.lab_week_05.ImageLoader

class GlideLoader(private val context: Context) : ImageLoader {
    override fun loadImage(imageUrl: String, imageView: ImageView) {
        if (imageUrl.isBlank()) {
            imageView.setImageResource(android.R.drawable.ic_dialog_alert)
            return
        }

        val requestOptions = RequestOptions()
            .placeholder(android.R.drawable.stat_sys_download)
            .error(android.R.drawable.ic_dialog_alert)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        try {
            Glide.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .into(imageView)
        } catch (e: Exception) {
            Log.e("GlideLoader", "EXCEPTION during Glide request setup: ${e.message}", e)
            imageView.setImageResource(android.R.drawable.ic_dialog_alert)
        }
    }
}
