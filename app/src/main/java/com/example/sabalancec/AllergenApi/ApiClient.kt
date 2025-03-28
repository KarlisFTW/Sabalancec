package com.example.sabalancec.AllergenApi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://rsc97lvk0erlhrp-y8b9c67gtggi5fdi.adb.eu-zurich-1.oraclecloudapps.com/"

    val warehouseApiService: WarehouseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WarehouseApiService::class.java)
    }
}
