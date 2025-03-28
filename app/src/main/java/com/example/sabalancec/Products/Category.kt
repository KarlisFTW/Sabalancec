package com.example.sabalancec.Products

import android.os.Parcelable
import com.example.sabalancec.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Int,
    val name: String,
    val imageResId: Int = R.drawable.carrot
) : Parcelable