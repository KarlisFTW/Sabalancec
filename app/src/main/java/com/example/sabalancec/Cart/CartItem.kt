package com.example.sabalancec.Cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val productId: Int,
    val name: String,
    val price: Double,
    val image: String,
    val imageRes: Int = 0,
    val amount: String,
    var quantity: Int = 1
) {
    fun getTotalPrice(): Double = price * quantity
}