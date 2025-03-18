package com.example.sabalancec.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sabalancec.Fragments.AccountFragment
import com.example.sabalancec.Fragments.AllergensFragment
import com.example.sabalancec.Fragments.CartFragment
import com.example.sabalancec.Fragments.CatalogueFragment
import com.example.sabalancec.Fragments.HomeFragment
import com.example.sabalancec.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class WhenLoggedIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_when_logged_in)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Default Fragment
        loadFragment(HomeFragment())

        // Set default colors
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.itemTextColor = null

        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_catalogue -> CatalogueFragment()
                R.id.nav_cart -> CartFragment()
                R.id.nav_allergens -> AllergensFragment()
                R.id.nav_account -> AccountFragment()
                else -> HomeFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
