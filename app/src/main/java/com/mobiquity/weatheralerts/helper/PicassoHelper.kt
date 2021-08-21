package com.mobiquity.weatheralerts.helper

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mobiquity.weatheralerts.R

object PicassoHelper {

    fun loadInto(ctx: Context, url: String?, v: ImageView) {
        if (url == null) {
            v.setImageDrawable(null)
            return
        }

        Glide.with(ctx)
            .load(url)
            .fitCenter()
            .centerCrop()
            .placeholder(R.drawable.image_placeholder)
            .into(v)
    }

    fun loadIntoNoPlaceholder(ctx: Context, url: String?, v: ImageView) {
        if (url == null) {
            v.setImageDrawable(null)
            return
        }

        Glide.with(ctx)
            .load(url)
            .fitCenter()
            .centerCrop()
            .placeholder(R.drawable.image_placeholder)
            .into(v)
    }

    fun loadIntoCircleNoPlaceholder(ctx: Context, url: String?, v: ImageView) {
        if (url == null) {
            return
        }

        Glide.with(ctx)
            .load(url)
            .fitCenter()
            .centerCrop()
            .circleCrop()
            .into(v)
    }
}