package com.example.sabalancec.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    val userName: String,
    val rating: Float,
    val comment: String,
    val date: String = ""
) : Parcelable