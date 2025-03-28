package com.example.sabalancec.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.sabalancec.Auth.AuthManager
import com.example.sabalancec.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class DeliveryAddressBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var authManager: AuthManager
    private lateinit var addressLineEditText: TextInputEditText
    private lateinit var cityEditText: TextInputEditText
    private lateinit var stateEditText: TextInputEditText
    private lateinit var countryEditText: TextInputEditText

    private lateinit var addressLineLayout: TextInputLayout
    private lateinit var cityLayout: TextInputLayout
    private lateinit var stateLayout: TextInputLayout
    private lateinit var countryLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_deliveryaddress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Apply rounded corners
        view.background = ContextCompat.getDrawable(requireContext(), R.drawable.bottom_sheet_rounded_corners)

        authManager = AuthManager.getInstance(requireContext())

        // Initialize layouts
        addressLineLayout = view.findViewById(R.id.txtfield_addressline)
        cityLayout = view.findViewById(R.id.txtfield_city)
        stateLayout = view.findViewById(R.id.txtfield_state)
        countryLayout = view.findViewById(R.id.txtfield_country)

        // Initialize edit texts
        addressLineEditText = addressLineLayout.editText as TextInputEditText
        cityEditText = cityLayout.editText as TextInputEditText
        stateEditText = stateLayout.editText as TextInputEditText
        countryEditText = countryLayout.editText as TextInputEditText

        val closeButton = view.findViewById<ImageButton>(R.id.imageButton)
        val saveButton = view.findViewById<MaterialButton>(R.id.btn_save)

        // Populate address fields
        val currentAddress = authManager.getUserAddress()
        if (!currentAddress.isNullOrEmpty()) {
            val parts = currentAddress.split(", ")
            if (parts.isNotEmpty()) addressLineEditText.setText(parts.getOrNull(0))
            if (parts.size > 1) cityEditText.setText(parts.getOrNull(1))
            if (parts.size > 2) stateEditText.setText(parts.getOrNull(2))
            if (parts.size > 3) countryEditText.setText(parts.getOrNull(3))
        }

        // Setup validation listeners
        setupValidationListeners()

        // Close button
        closeButton.setOnClickListener { dismiss() }

        // Save button with validation
        saveButton.setOnClickListener {
            if (validateAllFields()) {
                val addressLine = addressLineEditText.text.toString().trim()
                val city = cityEditText.text.toString().trim()
                val state = stateEditText.text.toString().trim()
                val country = countryEditText.text.toString().trim()

                // Format complete address
                val fullAddress = "$addressLine, $city, $state, $country".trimEnd(',', ' ')

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
    }

    private fun setupValidationListeners() {
        addressLineEditText.addTextChangedListener(createTextWatcher { validateAddressLine() })
        cityEditText.addTextChangedListener(createTextWatcher { validateCity() })
        stateEditText.addTextChangedListener(createTextWatcher { validateState() })
        countryEditText.addTextChangedListener(createTextWatcher { validateCountry() })
    }

    private fun createTextWatcher(validationFunction: () -> Boolean): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { validationFunction() }
        }
    }

    private fun validateAddressLine(): Boolean {
        val addressLine = addressLineEditText.text.toString().trim()
        return when {
            addressLine.isEmpty() -> {
                addressLineLayout.error = "Address is required"
                false
            }
            addressLine.length < 5 -> {
                addressLineLayout.error = "Address must be at least 5 characters"
                false
            }
            else -> {
                addressLineLayout.error = null
                true
            }
        }
    }

    private fun validateCity(): Boolean {
        val city = cityEditText.text.toString().trim()
        return when {
            city.isEmpty() -> {
                cityLayout.error = "City is required"
                false
            }
            city.length < 2 -> {
                cityLayout.error = "City must be at least 2 characters"
                false
            }
            else -> {
                cityLayout.error = null
                true
            }
        }
    }

    private fun validateState(): Boolean {
        val state = stateEditText.text.toString().trim()
        if (state.isNotEmpty() && state.length < 2) {
            stateLayout.error = "State must be at least 2 characters"
            return false
        }
        stateLayout.error = null
        return true
    }

    private fun validateCountry(): Boolean {
        val country = countryEditText.text.toString().trim()
        if (country.isNotEmpty() && country.length < 2) {
            countryLayout.error = "Country must be at least 2 characters"
            return false
        }
        countryLayout.error = null
        return true
    }

    private fun validateAllFields(): Boolean {
        return validateAddressLine() && validateCity() && validateState() && validateCountry()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }
}