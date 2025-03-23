package com.example.sabalancec.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sabalancec.Fragments.AddPhoneNumberFragment
import com.example.sabalancec.Fragments.VerifyPhoneNumberFragment
import com.example.sabalancec.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddPhoneNumberActivity : AppCompatActivity() {

    private lateinit var backButton: FloatingActionButton
    private var currentStep = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_phone_nr)

        backButton = findViewById(R.id.floatingActionButton)

        // Set up back button click listener
        backButton.setOnClickListener {
            handleBackPress()
        }

        // Start with the first sub-fragment if not already added
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AddPhoneNumberFragment())
                .commit()
        }

        // Setup listeners after fragments are created
        supportFragmentManager.executePendingTransactions()

        // Get reference to the phone number sub-fragment
        val phoneNumberSubFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as? AddPhoneNumberFragment

        // Set up listener for continue button in the phone number sub-fragment
        phoneNumberSubFragment?.setOnContinueClickListener {
            navigateToVerificationStep(it)
        }
    }

    private fun navigateToVerificationStep(phoneNumber: String) {
        // Create and show the verification sub-fragment
        val verifyFragment = VerifyPhoneNumberFragment().apply {
            // You can pass the phone number as an argument if needed
            arguments = Bundle().apply {
                putString("phone_number", phoneNumber)
            }

            // Set up listener for the verification fragment's continue button
            setOnVerificationCompleteListener {
                // Launch WhenLoggedIn activity and finish this one
                val intent = Intent(this@AddPhoneNumberActivity, WhenLoggedIn::class.java)
                startActivity(intent)
                finish()
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, verifyFragment)
            .commit()

        currentStep = 2
    }

    private fun handleBackPress() {
        if (currentStep == 2) {
            // Go back to the first step
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AddPhoneNumberFragment())
                .commit()
            currentStep = 1
        } else {
            // Finish the activity
            finish()
        }
    }

    override fun onBackPressed() {
        handleBackPress()
        // Don't call super.onBackPressed() here as it will cause the activity to finish
        // regardless of our custom back handling
    }
}