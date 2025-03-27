package com.example.sabalancec.Products

import com.example.sabalancec.models.Review

object ProductCache {
    // In-memory cache
    private val productCache = mutableMapOf<Int, Product>()
    private val productsByCategory = mutableMapOf<String, List<Product>>()
    private var allProducts: List<Product>? = null

    // Cache timeout (5 minutes)
    private const val CACHE_TIMEOUT = 5 * 60 * 1000L  // 5 minutes in milliseconds
    private var lastCacheTime: Long = 0

    fun isCacheValid(): Boolean {
        return System.currentTimeMillis() - lastCacheTime < CACHE_TIMEOUT
    }

    fun saveProducts(products: List<Product>) {
        allProducts = products
        lastCacheTime = System.currentTimeMillis()

        // Update individual product cache
        for (product in products) {
            productCache[product.id] = product
        }

        // Update category cache
        val categoryGroups = products.groupBy { it.categoryId }
        productsByCategory.clear()
        productsByCategory.putAll(categoryGroups)
    }

    fun getAllProducts(): List<Product>? = allProducts

    fun getProduct(id: Int): Product? = productCache[id]

    fun getProductsByCategory(categoryId: String): List<Product> =
        productsByCategory[categoryId] ?: emptyList()

    fun clearCache() {
        productCache.clear()
        productsByCategory.clear()
        allProducts = null
        lastCacheTime = 0
    }
}