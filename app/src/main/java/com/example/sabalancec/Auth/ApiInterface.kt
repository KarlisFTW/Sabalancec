package com.example.sabalancec.Auth

import com.example.sabalancec.Auth.models.AuthResponse
import com.example.sabalancec.Auth.models.LoginRequest
import com.example.sabalancec.Auth.models.RefreshTokenRequest
import com.example.sabalancec.Auth.models.RegisterRequest
import com.example.sabalancec.Auth.models.TokenResponse
import com.example.sabalancec.Auth.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterRequest>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<TokenResponse>

    @POST("logout")
    suspend fun logout(@Body request: RefreshTokenRequest): Response<Unit>

    @GET("user")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<UserResponse>

    @PUT("user")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body request: Map<String, String>
    ): Response<Unit>
}