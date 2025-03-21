package com.example.sabalancec.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sabalancec.Fragments.AuthOptionsFragment
import com.example.sabalancec.R
import com.example.sabalancec.Auth.AuthManager
import com.example.sabalancec.Auth.models.AuthResponse
import com.example.sabalancec.Fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authenticationFragment = R.id.frag_authScreens
        val whenLoggedInFragment = R.id.fragment_container

        //Check if the user has authenticated
        val authManager = AuthManager.getInstance(this)
        if (authManager.isTokenValid()) {
            // User is already logged in, navigate to the main content
//            setContentView(R.layout.activity_when_logged_in)
//            loadFragment(HomeFragment(), whenLoggedInFragment)
//            return
            val intent = Intent(this, WhenLoggedIn::class.java)
            startActivity(intent)
        }
        else
        {
            // User is not logged in, navigate to the authentication screens
            setContentView(R.layout.activity_auth)
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
