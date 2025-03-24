package com.example.sabalancec.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.sabalancec.Auth.AuthManager
import com.example.sabalancec.Fragments.AuthOptionsFragment
import com.example.sabalancec.R
import com.example.sabalancec.Auth.models.AuthResponse
import com.example.sabalancec.Fragments.HomeFragment
import kotlinx.coroutines.launch
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Optional: if you need to keep the splash screen visible longer
        // splashScreen.setKeepOnScreenCondition { true }
        // Later call: yourCondition = false (when ready to dismiss)

        setContentView(R.layout.activity_auth)  // Set default layout

        val authenticationFragment = R.id.frag_authScreens
        val authManager = AuthManager.getInstance(this)

        // First quick check with local data only
        if (authManager.getAccessToken() != null) {
            // Token exists, launch coroutine to properly validate it
            lifecycleScope.launch {
                val isTokenValid = try {
                    authManager.validateToken()
                } catch (e: Exception) {
                    false
                }

                if (isTokenValid) {
                    // Valid token, navigate to main screen
                    val intent = Intent(this@MainActivity, WhenLoggedIn::class.java)
                    startActivity(intent)
                    finish()  // Close MainActivity
                } else {
                    // Invalid token, show auth fragment
                    loadFragment(AuthOptionsFragment(), authenticationFragment)
                }
            }
        } else {
            // No token, load auth fragment immediately
            loadFragment(AuthOptionsFragment(), authenticationFragment)
        }







//        val btLoginTemp: Button = findViewById(R.id.btn_signin) // Correct button initialization
//
//        btLoginTemp.setOnClickListener {
//            val intent = Intent(this, WhenLoggedIn::class.java)
//            startActivity(intent)
//        }
    }

    private fun loadFragment(fragment: Fragment, fragmentContainer: Int) {
        supportFragmentManager.beginTransaction()
            .replace(fragmentContainer, fragment)
            .commit()
    }
}
