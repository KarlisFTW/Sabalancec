package com.example.sabalancec.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.sabalancec.Auth.AuthManager
import com.example.sabalancec.Fragments.AuthOptionsFragment
import com.example.sabalancec.R
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

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
                    val intent = Intent(this@WelcomeActivity, WhenLoggedIn::class.java)
                    startActivity(intent)
                    finish()  // Close MainActivity
                }
            }
        }

        setContentView(R.layout.activity_welcome_screen)

        // Set up the Get Started button
        val btnGetStarted: Button = findViewById(R.id.btn_getstarted)
        btnGetStarted.setOnClickListener {
            // Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close this activity so user can't go back
        }
    }

//    private fun loadFragment(fragment: Fragment, fragmentContainer: Int) {
//        supportFragmentManager.beginTransaction()
//            .replace(fragmentContainer, fragment)
//            .commit()
//    }
}