package com.example.sabalancec.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
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

class MyDetailsBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var authManager: AuthManager
    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var dateOfBirthEditText: TextInputEditText

    private lateinit var nameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var dateOfBirthLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_mydetails, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Apply rounded corners
        view.background = ContextCompat.getDrawable(requireContext(), R.drawable.bottom_sheet_rounded_corners)

        authManager = AuthManager.getInstance(requireContext())

        // Initialize layouts
        nameLayout = view.findViewById(R.id.txtfield_name)
        emailLayout = view.findViewById(R.id.txtfield_email)
        dateOfBirthLayout = view.findViewById(R.id.txtfield_dateofbirth)

        // Initialize edit texts
        nameEditText = nameLayout.editText as TextInputEditText
        emailEditText = emailLayout.editText as TextInputEditText
        dateOfBirthEditText = dateOfBirthLayout.editText as TextInputEditText

        val closeButton = view.findViewById<ImageButton>(R.id.imageButton)
        val saveButton = view.findViewById<MaterialButton>(R.id.btn_save)

        // Populate fields
        nameEditText.setText(authManager.getUserFullName())
        emailEditText.setText(authManager.getUserEmail())

        // Setup validation listeners
        setupValidationListeners()

        // Close button
        closeButton.setOnClickListener { dismiss() }

        // Save button with validation
        saveButton.setOnClickListener {
            if (validateAllFields()) {
                val name = nameEditText.text.toString().trim()
                val email = emailEditText.text.toString().trim()

                lifecycleScope.launch {
                    val address = authManager.getUserAddress() ?: ""
                    val result = authManager.updateUserProfile(name, email, address)

                    if (result) {
                        Toast.makeText(context, "Details updated successfully", Toast.LENGTH_SHORT).show()
                        (parentFragment as? AccountFragment)?.displayUserInfo()
                        dismiss()
                    } else {
                        Toast.makeText(context, "Failed to update details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun setupValidationListeners() {
        nameEditText.addTextChangedListener(createTextWatcher { validateName() })
        emailEditText.addTextChangedListener(createTextWatcher { validateEmail() })
    }

    private fun createTextWatcher(validationFunction: () -> Boolean): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { validationFunction() }
        }
    }

    private fun validateName(): Boolean {
        val name = nameEditText.text.toString().trim()
        return when {
            name.isEmpty() -> {
                nameLayout.error = "Name is required"
                false
            }
            name.length < 2 -> {
                nameLayout.error = "Name must be at least 2 characters"
                false
            }
            else -> {
                nameLayout.error = null
                true
            }
        }
    }

    private fun validateEmail(): Boolean {
        val email = emailEditText.text.toString().trim()
        return when {
            email.isEmpty() -> {
                emailLayout.error = "Email is required"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailLayout.error = "Enter a valid email address"
                false
            }
            else -> {
                emailLayout.error = null
                true
            }
        }
    }

    private fun validateAllFields(): Boolean {
        return validateName() && validateEmail()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }
}