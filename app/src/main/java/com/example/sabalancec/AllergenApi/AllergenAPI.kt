package com.example.sabalancec.AllergenApi

import retrofit2.Response
import retrofit2.http.GET

interface WarehouseApiService {
    @GET("ords/warehouse/api/allergens")
    suspend fun getAllergens(): Response<AllergenResponse>

}
