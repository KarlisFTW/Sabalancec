package com.example.sabalancec.Auth.models

data class AuthResponse(
    val id: String,
    val fullName: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)