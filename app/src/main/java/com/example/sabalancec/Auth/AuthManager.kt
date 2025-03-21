package com.example.sabalancec.Auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.sabalancec.Auth.models.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class AuthManager private constructor(context: Context) {
    private val TAG = "AuthManager"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_FULL_NAME = "full_name"
        private const val KEY_EMAIL = "email"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val API_BASE_URL = "http://13.53.35.231/api"  // Changed to http from https
        private const val TAG = "AuthManager"

        @Volatile
        private var instance: AuthManager? = null

        fun getInstance(context: Context): AuthManager {
            Log.d(TAG, "getInstance() called")
            return instance ?: synchronized(this) {
                Log.d(TAG, "Creating new AuthManager instance")
                instance ?: AuthManager(context.applicationContext).also { instance = it }
            }
        }
    }

    suspend fun login(email: String, password: String): Boolean {
        Log.d(TAG, "login() called with email: $email")
        return withContext(Dispatchers.IO) {
            try {
                val loginUrl = "$API_BASE_URL/login"
                Log.d(TAG, "Attempting to connect to: $loginUrl")

                val url = URL(loginUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                Log.d(TAG, "Connection established, preparing request body")

                // Create request body
                val jsonBody = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                }

                val requestBody = jsonBody.toString()
                Log.d(TAG, "Request body: $requestBody")

                // Send request
                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(requestBody)
                }

                Log.d(TAG, "Request sent, checking response")

                // Check response
                val responseCode = connection.responseCode
                Log.d(TAG, "Response code: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Parse the response
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    Log.d(TAG, "Response received: $response")

                    val jsonResponse = JSONObject(response)
                    Log.d(TAG, "Parsed JSON response successfully")

                    // Save auth data
                    val authResponse = AuthResponse(
                        id = jsonResponse.getString("id"),
                        fullName = jsonResponse.getString("fullName"),
                        email = jsonResponse.getString("email"),
                        accessToken = jsonResponse.getString("accessToken"),
                        refreshToken = jsonResponse.getString("refreshToken")
                    )

                    Log.d(TAG, "Created AuthResponse object: id=${authResponse.id}, name=${authResponse.fullName}")
                    saveAuthData(authResponse)
                    Log.d(TAG, "Login successful")
                    true
                } else {
                    val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "No error details"
                    Log.e(TAG, "Login failed with code $responseCode. Error: $errorResponse")
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during login: ${e.javaClass.simpleName}: ${e.message}")
                e.printStackTrace()
                false
            }
        }
    }

    fun saveAuthData(authResponse: AuthResponse) {
        Log.d(TAG, "saveAuthData() called")
        sharedPreferences.edit().apply {
            putString(KEY_USER_ID, authResponse.id)
            putString(KEY_FULL_NAME, authResponse.fullName)
            putString(KEY_EMAIL, authResponse.email)
            putString(KEY_ACCESS_TOKEN, authResponse.accessToken)
            putString(KEY_REFRESH_TOKEN, authResponse.refreshToken)
            apply()
        }
        Log.d(TAG, "Auth data saved to SharedPreferences")
    }

    fun getAuthData(): AuthResponse? {
        Log.d(TAG, "getAuthData() called")
        val userId = sharedPreferences.getString(KEY_USER_ID, null)
        if (userId == null) {
            Log.d(TAG, "No user ID found in SharedPreferences")
            return null
        }

        val authResponse = AuthResponse(
            id = userId,
            fullName = sharedPreferences.getString(KEY_FULL_NAME, "") ?: "",
            email = sharedPreferences.getString(KEY_EMAIL, "") ?: "",
            accessToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, "") ?: "",
            refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, "") ?: ""
        )

        Log.d(TAG, "Retrieved auth data: id=${authResponse.id}, email=${authResponse.email}")
        return authResponse
    }

    fun isLoggedIn(): Boolean {
        val isLoggedIn = sharedPreferences.contains(KEY_ACCESS_TOKEN)
        Log.d(TAG, "isLoggedIn() = $isLoggedIn")
        return isLoggedIn
    }

    fun logout() {
        Log.d(TAG, "logout() called")
        sharedPreferences.edit().clear().apply()
        Log.d(TAG, "SharedPreferences cleared")
    }

    fun getAccessToken(): String? {
        val token = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        Log.d(TAG, "getAccessToken() called, token ${if (token != null) "exists" else "is null"}")
        if (token != null) {
            Log.d(TAG, "Token starts with: ${token.take(10)}...")
        }
        return token
    }

    fun isTokenValid(): Boolean {
        Log.d(TAG, "isTokenValid() called")
        // TODO: Implement proper token validation (check expiry, etc.)
        val valid = isLoggedIn()
        Log.d(TAG, "Token validity check result: $valid")
        return valid
    }
}