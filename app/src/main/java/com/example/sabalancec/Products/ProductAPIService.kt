package com.example.sabalancec.Products

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("api/products")
    suspend fun getProducts(): ProductResponse

    @GET("api/categories")
    suspend fun getCategories(): CategoryResponse
}

class ProductRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rsc97lvk0erlhrp-y8b9c67gtggi5fdi.adb.eu-zurich-1.oraclecloudapps.com/ords/warehouse/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun getProducts(forceRefresh: Boolean = false): List<Product> {
        // Return cached data if available and not forcing refresh
        if (!forceRefresh && ProductCache.isCacheValid()) {
            ProductCache.getAllProducts()?.let {
                return it
            }
        }

        // Otherwise fetch from API
        val categoryMap = getCategoryMap()
        val productResponse = apiService.getProducts()

        val products = productResponse.items.map { item ->
            Product(
                id = item.id,
                name = item.name,
                price = item.price,
                categoryId = item.categoryId,
                image = item.image,
                amountSold = item.amountSold,
                availableQuantity = item.availableQuantity,
                hasAllergens = item.hasAllergens,
                allergenId = item.allergenId,
                rating = item.rating,
                usersId = item.usersId
            )
        }

        // Save to cache
        ProductCache.saveProducts(products)
        return products
    }

//    suspend fun getProductsByCategory(categoryId: Int, forceRefresh: Boolean = false): List<Product> {
//        val allProducts = getProducts(forceRefresh)
//        return allProducts.filter { it.categoryId == categoryId }
//    }

    suspend fun getCategories(): CategoryResponse {
        return apiService.getCategories()
    }

    suspend fun getProductById(productId: Int, forceRefresh: Boolean = false): Product? {
        // Check cache first
        if (!forceRefresh) {
            ProductCache.getProduct(productId)?.let {
                return it
            }
        }

        // If not in cache or forced refresh, get all products (which updates cache)
        getProducts(forceRefresh)
        return ProductCache.getProduct(productId)
    }

    private suspend fun getCategoryMap(): Map<String, Int> {
        val categoryResponse = apiService.getCategories()
        return categoryResponse.items.associate { it.category to it.id }
    }
}