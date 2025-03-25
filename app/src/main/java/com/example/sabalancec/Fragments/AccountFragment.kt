package com.example.sabalancec.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sabalancec.R
import com.example.sabalancec.Auth.AuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountFragment : Fragment() {

    private lateinit var authManager: AuthManager
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var addressTextView: TextView

    private lateinit var btnProfileInfo: Button
    private lateinit var btnOrderHistory: Button
    private lateinit var btnSettings: Button
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        // Init views
        usernameTextView = view.findViewById(R.id.txt_username)
        emailTextView = view.findViewById(R.id.txt_useremail)
        addressTextView = view.findViewById(R.id.txt_useraddress)

        val btnProfileInfo = view.findViewById<TextView>(R.id.btn_profile_info)
        val btnOrderHistory = view.findViewById<TextView>(R.id.btn_order_history)
        val btnSettings = view.findViewById<TextView>(R.id.btn_settings)
        val btnLogout = view.findViewById<TextView>(R.id.btn_logout)

        // Auth manager
        authManager = AuthManager.getInstance(requireContext())

        // Display user info from SharedPreferences
        displayBasicUserInfo()

        // Set click listeners
        btnProfileInfo.setOnClickListener {
            Toast.makeText(requireContext(), "Profile Info clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Profile Info screen
        }

        btnOrderHistory.setOnClickListener {
            Toast.makeText(requireContext(), "Order History clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Order History screen
        }

        btnSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Payment & Account Settings screen
        }

        //btnLogout.setOnClickListener {
           // authManager.logout()
           // Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Login screen
      //  }

        // TODO: Add swipe-to-refresh if needed

        return view
    }

    override fun onResume() {
        super.onResume()
        displayBasicUserInfo()
    }

    private fun displayBasicUserInfo() {
        val name = authManager.getUserFullName() ?: "Not available"
        val email = authManager.getUserEmail() ?: "Not available"
        val address = authManager.getUserAddress() ?: "Not available"

        usernameTextView.text = name
        emailTextView.text = email
        addressTextView.text = address
    }

    // Optional: keep this if you want to use it later
    private fun fetchUserProfile() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userProfile = authManager.getUserProfileSafe()

                withContext(Dispatchers.Main) {
                    if (userProfile != null) {
                        usernameTextView.text = userProfile.fullName
                        emailTextView.text = userProfile.email
                        addressTextView.text = userProfile.address
                    } else {
                        addressTextView.text = "Address not available"
                        Toast.makeText(requireContext(), "Could not retrieve full profile", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
