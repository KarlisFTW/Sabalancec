package com.example.sabalancec.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        usernameTextView = view.findViewById(R.id.txt_username)
        emailTextView = view.findViewById(R.id.txt_useremail)
        addressTextView = view.findViewById(R.id.txt_useraddress)

        authManager = AuthManager.getInstance(requireContext())

        // Display basic info immediately from SharedPreferences
        displayBasicUserInfo()

        // Fetch complete profile from API
        fetchUserProfile()

        return view
    }

    private fun displayBasicUserInfo() {
        // Get stored user info from SharedPreferences
        val name = authManager.getUserFullName() ?: "Not available"
        val email = authManager.getUserEmail() ?: "Not available"
        val address = authManager.getUserAddress() ?: "Not available"

        usernameTextView.text = name
        emailTextView.text = email
        addressTextView.text = address
    }

    private fun fetchUserProfile() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userProfile = authManager.getUserProfileSafe()

                withContext(Dispatchers.Main) {
                    if (userProfile != null) {
                        // Update UI with complete profile information
                        usernameTextView.text = userProfile.fullName
                        emailTextView.text = userProfile.email
                        addressTextView.text = userProfile.address
                    } else {
                        // Failed to get profile, show locally stored data only
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

    override fun onResume() {
        super.onResume()
        // Refresh profile data when fragment becomes visible
        fetchUserProfile()
    }
}