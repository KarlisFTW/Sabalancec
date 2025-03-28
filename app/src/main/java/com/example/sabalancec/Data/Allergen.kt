package com.example.sabalancec.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Allergen(
    val id: Int,
    val name: String,
    val image: String,
    val info: Int,
    val description: String? = null,
    val reaction: String? = null
) : Parcelable
