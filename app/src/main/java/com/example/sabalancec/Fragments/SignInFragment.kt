package com.example.sabalancec.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sabalancec.Activities.WhenLoggedIn
import com.example.sabalancec.Auth.AuthManager
import com.example.sabalancec.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInFragment : Fragment() {
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordInput: TextInputEditText
    private lateinit var signInButton: MaterialButton
    private lateinit var authManager: AuthManager
    private lateinit var signUpLink: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signin, container, false)

        // Initialize the AuthManager
        authManager = AuthManager.getInstance(requireContext())

        // Initialize views
        emailInputLayout = view.findViewById(R.id.txtfield_email)
        emailInput = emailInputLayout.editText as TextInputEditText
        passwordInputLayout = view.findViewById(R.id.txtfield_password)
        passwordInput = passwordInputLayout.editText as TextInputEditText
        signInButton = view.findViewById(R.id.btn_signin)

        // Set up sign in button click listener
        signInButton.setOnClickListener {
            attemptLogin()
        }

        // Set up the sign up link
        signUpLink = view.findViewById(R.id.tv_signup_link)
        signUpLink.setOnClickListener {
            // Replace current fragment with SignUpFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.frag_authScreens, SignUpFragment())
                .commit()
        }

        return view
    }

    private fun attemptLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        // Basic validation
        var isValid = true

        if (email.isEmpty()) {
            emailInputLayout.error = "Email is required"
            isValid = false
        } else {
            emailInputLayout.error = null
        }

        if (password.isEmpty()) {
            passwordInputLayout.error = "Password is required"
            isValid = false
        } else {
            passwordInputLayout.error = null
        }

        if (isValid) {
            // Show loading state
            signInButton.isEnabled = false
            signInButton.text = "Signing in..."
            Log.d("SignInFragment", "Attempting login with email: $email")

            // Attempt login via AuthManager
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val loginResult = authManager.login(email, password)

                    withContext(Dispatchers.Main) {
                        if (loginResult) {
                            // Navigate to main screen on success
                            navigateToMainScreen()
                        } else {
                            Toast.makeText(requireContext(), "Login failed. Please check your credentials.", Toast.LENGTH_LONG).show()
                            // Reset button state
                            signInButton.isEnabled = true
                            signInButton.text = "Sign In"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        // Reset button state
                        signInButton.isEnabled = true
                        signInButton.text = "Sign In"
                    }
                }
            }
        }
    }

    private fun navigateToMainScreen() {
        // Navigate to the WhenLoggedIn activity
        val intent = Intent(requireContext(), WhenLoggedIn::class.java)
        startActivity(intent)
        requireActivity().finish() // Close the current activity to prevent going back
    }
}