package com.example.sabalancec.Cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.example.sabalancec.Products.Product
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
    val cartItems: LiveData<List<CartItem>> = cartRepository.allCartItems.asLiveData()
    val totalPrice: LiveData<Double> = cartRepository.totalPrice.map { it ?: 0.0 }.asLiveData()
    val cartItemCount: LiveData<Int> = cartRepository.cartItemCount.asLiveData()

    fun addToCart(product: Product) = viewModelScope.launch {
        cartRepository.addToCart(product)
    }

    fun increaseQuantity(productId: Int) = viewModelScope.launch {
        cartRepository.increaseQuantity(productId)
    }

    fun decreaseQuantity(productId: Int) = viewModelScope.launch {
        cartRepository.decreaseQuantity(productId)
    }

    fun removeFromCart(productId: Int) = viewModelScope.launch {
        cartRepository.removeFromCart(productId)
    }

    fun clearCart() = viewModelScope.launch {
        cartRepository.clearCart()
    }

    fun isProductInCart(productId: Int): LiveData<Boolean> {
        return cartRepository.isInCart(productId).asLiveData()
    }
}

class CartViewModelFactory(private val repository: CartRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}