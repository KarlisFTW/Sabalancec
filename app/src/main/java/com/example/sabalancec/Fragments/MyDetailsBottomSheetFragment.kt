package com.example.sabalancec.Fragments

import android.os.Bundle
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_mydetails, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Apply rounded corners to the bottom sheet
        view.background = ContextCompat.getDrawable(requireContext(), R.drawable.bottom_sheet_rounded_corners)

        authManager = AuthManager.getInstance(requireContext())

        // Initialize views
        nameEditText = view.findViewById<TextInputLayout>(R.id.txtfield_name).editText as TextInputEditText
        emailEditText = view.findViewById<TextInputLayout>(R.id.txtfield_email).editText as TextInputEditText
        dateOfBirthEditText = view.findViewById<TextInputLayout>(R.id.txtfield_dateofbirth).editText as TextInputEditText
        val closeButton = view.findViewById<ImageButton>(R.id.imageButton)
        val saveButton = view.findViewById<MaterialButton>(R.id.btn_save)

        // Populate fields with current user data
        nameEditText.setText(authManager.getUserFullName())
        emailEditText.setText(authManager.getUserEmail())

        // Set up close button
        closeButton.setOnClickListener { dismiss() }

        // Set up save button
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Launch coroutine for API call
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

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }
}