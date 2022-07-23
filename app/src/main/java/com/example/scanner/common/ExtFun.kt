package com.example.scanner.common

import android.widget.ImageView
import com.example.scanner.R

fun ImageView.loadImageFromNetwork(productId: Int, url: String) {
    /*Glide.with(this)  //load image from url
        .load(url)
        .centerCrop()
        .into(this)*/

    //showing image from drawables for example
    this.setImageResource(
        when (productId) {
            1 -> R.drawable.product_1
            2 -> R.drawable.product_4
            3 -> R.drawable.product_3
            4 -> R.drawable.product_2
            else -> 0
        }
    )
}
