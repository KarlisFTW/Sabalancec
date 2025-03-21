package com.example.sabalancec.Auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.sabalancec.Auth.models.AuthResponse
import com.example.sabalancec.Auth.models.LoginRequest
import com.example.sabalancec.Auth.models.RefreshTokenRequest
import com.example.sabalancec.Auth.models.RegisterRequest
import com.example.sabalancec.Auth.models.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit

class AuthManager private constructor(context: Context) {
    private val TAG = "AuthManager"
    private val BASE_URL = "http://13.53.35.231/api/"
    private val PREFS_NAME = "auth_prefs"
    private val KEY_ACCESS_TOKEN = "access_token"
    private val KEY_REFRESH_TOKEN = "refresh_token"
    private val KEY_TOKEN_EXPIRY = "token_expiry"
    private val KEY_USER_ID = "user_id"
    private val KEY_USER_EMAIL = "user_email"
    private val KEY_USER_FULL_NAME = "user_full_name"

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val appContext = context.applicationContext

    private val apiService: AuthApiService

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(AuthApiService::class.java)
    }

    // Method to register a new user
    suspend fun register(fullName: String, email: String, address: String, password: String): Boolean {
        return try {
            val request = RegisterRequest(null, fullName, email, address, password)
            val response = apiService.register(request)

            if (response.isSuccessful) {
                Log.d(TAG, "Registration successful")
                true
            } else {
                Log.e(TAG, "Registration failed: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Registration error: ${e.message}")
            false
        }
    }

    // Method to login user
    suspend fun login(email: String, password: String): Boolean {
        return try {
            val loginRequest = LoginRequest(email, password)
            val response = apiService.login(loginRequest)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                saveAuthDetails(authResponse)
                Log.d(TAG, "Login successful")
                true
            } else {
                Log.e(TAG, "Login failed: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Login error: ${e.message}")
            false
        }
    }

    // Method to refresh token
    suspend fun refreshToken(): Boolean {
        val refreshToken = getRefreshToken() ?: return false

        return try {
            val request = RefreshTokenRequest(refreshToken)
            val response = apiService.refreshToken(request)

            if (response.isSuccessful && response.body() != null) {
                val tokenResponse = response.body()!!

                // Update stored tokens
                sharedPreferences.edit().apply {
                    putString(KEY_ACCESS_TOKEN, tokenResponse.accessToken)
                    putString(KEY_REFRESH_TOKEN, tokenResponse.refreshToken)
                    // Set expiry time (assuming 1 hour validity)
                    putLong(KEY_TOKEN_EXPIRY, System.currentTimeMillis() + 3600 * 1000)
                    apply()
                }

                Log.d(TAG, "Token refreshed successfully")
                true
            } else {
                Log.e(TAG, "Token refresh failed: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Token refresh error: ${e.message}")
            false
        }
    }

    // Method to logout
    suspend fun logout(): Boolean {
        val refreshToken = getRefreshToken() ?: return true

        return try {
            val request = RefreshTokenRequest(refreshToken)
            val response = apiService.logout(request)

            // Regardless of response, clear local credentials
            clearAuthDetails()

            if (response.isSuccessful) {
                Log.d(TAG, "Logout successful")
                true
            } else {
                Log.e(TAG, "Logout failed on server: ${response.errorBody()?.string()}")
                // Still return true as we've cleared local credentials
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logout error: ${e.message}")
            // Still clear credentials on error
            clearAuthDetails()
            true
        }
    }

    // Get user profile
    suspend fun getUserProfile(): UserResponse? {
        if (!isTokenValid()) {
            val refreshed = refreshToken()
            if (!refreshed) return null
        }

        return try {
            val token = "Bearer ${getAccessToken()}"
            val response = apiService.getUserProfile(token)

            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(TAG, "Failed to get user profile: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user profile: ${e.message}")
            null
        }
    }

    // Update user profile
    suspend fun updateUserProfile(fullName: String, email: String, address: String): Boolean {
        if (!isTokenValid()) {
            val refreshed = refreshToken()
            if (!refreshed) return false
        }

        return try {
            val token = "Bearer ${getAccessToken()}"
            val updateRequest = mapOf(
                "fullName" to fullName,
                "email" to email,
                "address" to address
            )

            val response = apiService.updateUserProfile(token, updateRequest)

            if (response.isSuccessful) {
                // Update stored user details
                sharedPreferences.edit().apply {
                    putString(KEY_USER_EMAIL, email)
                    putString(KEY_USER_FULL_NAME, fullName)
                    apply()
                }
                true
            } else {
                Log.e(TAG, "Failed to update profile: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating profile: ${e.message}")
            false
        }
    }

    // Check if token is valid
    fun isTokenValid(): Boolean {
        val expiryTime = sharedPreferences.getLong(KEY_TOKEN_EXPIRY, 0)
        val accessToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)

        // Check if token exists and hasn't expired
        return accessToken != null && System.currentTimeMillis() < expiryTime
    }

    // Get authenticated user ID
    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    // Get authenticated user email
    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    // Get authenticated user name
    fun getUserFullName(): String? {
        return sharedPreferences.getString(KEY_USER_FULL_NAME, null)
    }

    // Get access token
    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    // Get refresh token
    private fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    // Save authentication details to SharedPreferences
    private fun saveAuthDetails(authResponse: AuthResponse) {
        sharedPreferences.edit().apply {
            putString(KEY_ACCESS_TOKEN, authResponse.accessToken)
            putString(KEY_REFRESH_TOKEN, authResponse.refreshToken)
            putString(KEY_USER_ID, authResponse.id)
            putString(KEY_USER_EMAIL, authResponse.email)
            putString(KEY_USER_FULL_NAME, authResponse.fullName)
            // Set expiry time (assuming 1 hour validity)
            putLong(KEY_TOKEN_EXPIRY, System.currentTimeMillis() + 3600 * 1000)
            apply()
        }
    }

    // Clear authentication details
    private fun clearAuthDetails() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        @Volatile
        private var instance: AuthManager? = null

        fun getInstance(context: Context): AuthManager {
            return instance ?: synchronized(this) {
                instance ?: AuthManager(context).also { instance = it }
            }
        }
    }
}