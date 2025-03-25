package com.example.sabalancec.Data

import android.os.Parcelable
import com.example.sabalancec.models.Review
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Product(
    val imageRes: Int,
    val name: String,
    val amount: String,
    val price: String,
    val description: String = "",  // Product description
    val nutritionValues: @RawValue Map<String, String> = mapOf(), // Nutritional information
    val reviews: List<Review> = listOf() // Reviews for this product
) : Parcelable
