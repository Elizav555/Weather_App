package com.example.weatherApp.presentation.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.weatherApp.R

@BindingAdapter("iconUrl")
fun loadImageForItem(view: ImageView, imageUrl: String?) {
    if (imageUrl == null) return
    view.load(imageUrl) {
        error(R.drawable.weather)
    }
}

@BindingAdapter("app:goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
