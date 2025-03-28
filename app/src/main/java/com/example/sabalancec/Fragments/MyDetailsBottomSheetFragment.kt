package com.example.sabalancec.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.sabalancec.Auth.AuthManager
import com.example.sabalancec.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class MyDetailsBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var authManager: AuthManager
    private lateinit var addressLineEditText: TextInputEditText
    private lateinit var cityEditText: TextInputEditText
    private lateinit var stateEditText: TextInputEditText
    private lateinit var countryEditText: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_deliveryaddress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager.getInstance(requireContext())

        // Initialize views
        addressLineEditText = view.findViewById<TextInputLayout>(R.id.txtfield_addressline).editText as TextInputEditText
        cityEditText = view.findViewById<TextInputLayout>(R.id.txtfield_city).editText as TextInputEditText
        stateEditText = view.findViewById<TextInputLayout>(R.id.txtfield_state).editText as TextInputEditText
        countryEditText = view.findViewById<TextInputLayout>(R.id.txtfield_country).editText as TextInputEditText
        val closeButton = view.findViewById<ImageButton>(R.id.imageButton)
        val saveButton = view.findViewById<MaterialButton>(R.id.btn_save)

        // Populate address field if available
        val currentAddress = authManager.getUserAddress()
        if (!currentAddress.isNullOrEmpty()) {
            // Basic parsing of address - adapt as needed for your format
            val parts = currentAddress.split(", ")
            if (parts.isNotEmpty()) addressLineEditText.setText(parts.getOrNull(0))
            if (parts.size > 1) cityEditText.setText(parts.getOrNull(1))
            if (parts.size > 2) stateEditText.setText(parts.getOrNull(2))
            if (parts.size > 3) countryEditText.setText(parts.getOrNull(3))
        }

        // Set up close button
        closeButton.setOnClickListener { dismiss() }

        // Set up save button
        saveButton.setOnClickListener {
            val addressLine = addressLineEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()
            val state = stateEditText.text.toString().trim()
            val country = countryEditText.text.toString().trim()

            if (addressLine.isEmpty() || city.isEmpty()) {
                Toast.makeText(context, "Please fill address and city", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Format complete address
            val fullAddress = "$addressLine, $city, $state, $country".trimEnd(',', ' ')

            // Launch coroutine for API call
            lifecycleScope.launch {
                val name = authManager.getUserFullName() ?: ""
                val email = authManager.getUserEmail() ?: ""
                val result = authManager.updateUserProfile(name, email, fullAddress)

                if (result) {
                    Toast.makeText(context, "Address updated successfully", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(context, "Failed to update address", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }
}