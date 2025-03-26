package com.example.sabalancec.Products

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val items: List<ProductItem>
)

data class ProductItem(
    val id: Int,
    val name: String,
    val price: Double,
    @SerializedName("category_id")
    val categoryId: Int,
    val image: String,
    @SerializedName("amount_sold")
    val amountSold: Int,
    @SerializedName("available_quantity")
    val availableQuantity: Int,
    @SerializedName("has_allergens")
    val hasAllergens: String,
    @SerializedName("allergen_id")
    val allergenId: Int?,
    val rating: Float?,
    @SerializedName("provider_id")
    val providerId: Int?
)

data class CategoryResponse(
    val items: List<CategoryItem>
)

data class CategoryItem(
    val id: Int,
    val category: String
)