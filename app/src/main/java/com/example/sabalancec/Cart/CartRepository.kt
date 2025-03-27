package com.example.sabalancec.Cart

import com.example.sabalancec.Products.Product

class CartRepository(private val cartDao: CartDao) {
    val allCartItems = cartDao.getAllItems()
    val totalPrice = cartDao.getTotalPrice()
    val cartItemCount = cartDao.getCartItemCount()

    fun isInCart(productId: Int) = cartDao.isInCart(productId)

    suspend fun addToCart(product: Product) {
        val cartItem = CartItem(
            productId = product.id,
            name = product.name,
            price = product.price,
            image = product.image,
            imageRes = product.imageRes,
            amount = product.amount,
            quantity = 1
        )
        cartDao.insertItem(cartItem)
    }

    suspend fun increaseQuantity(productId: Int) {
        cartDao.incrementQuantity(productId)
    }

    suspend fun decreaseQuantity(productId: Int) {
        cartDao.decrementQuantity(productId)
    }

    suspend fun removeFromCart(productId: Int) {
        cartDao.deleteItem(productId)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}