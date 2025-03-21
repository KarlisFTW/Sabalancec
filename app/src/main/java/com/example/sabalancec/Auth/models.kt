package com.example.sabalancec.Auth.models

data class RegisterRequest(
    val id: String?,
    val fullName: String,
    val email: String,
    val address: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class AuthResponse(
    val id: String,
    val fullName: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)

data class UserResponse(
    val id: String,
    val fullName: String,
    val email: String,
    val address: String
)