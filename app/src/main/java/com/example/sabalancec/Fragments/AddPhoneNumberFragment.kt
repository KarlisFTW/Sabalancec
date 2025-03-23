package com.example.sabalancec.Fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sabalancec.Activities.WhenLoggedIn
import com.example.sabalancec.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker

class AddPhoneNumberFragment : Fragment() {

    private lateinit var phoneNumberInput: TextInputEditText
    private lateinit var phoneNumberLayout: TextInputLayout
    private lateinit var continueButton: FloatingActionButton
    private lateinit var skipButton: View
    private lateinit var countryCodePicker: CountryCodePicker

    private var onContinueClickListener: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_phone_nr, container, false)

        // Initialize views
        countryCodePicker = view.findViewById(R.id.country_code_picker)
        phoneNumberInput = view.findViewById(R.id.et_phone_number)
        phoneNumberLayout = view.findViewById(R.id.txtfield_phoneNr)
        continueButton = view.findViewById(R.id.btn_continue)
        skipButton = view.findViewById(R.id.txt_skip)

        setupPhoneNumberInput()

        continueButton.setOnClickListener {
            if (validatePhoneNumber()) {
                val fullNumber = countryCodePicker.selectedCountryCodeWithPlus +
                        phoneNumberInput.text.toString().trim()
                onContinueClickListener?.invoke(fullNumber)
            }
        }

        skipButton.setOnClickListener {
            // Skip verification and invoke the listener with an empty string
            //onContinueClickListener?.invoke("")
            val intent = Intent(requireContext(), WhenLoggedIn::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

    fun setOnContinueClickListener(listener: (String) -> Unit) {
        onContinueClickListener = listener
    }

    private fun setupPhoneNumberInput() {
        // Set up country code picker listener
        countryCodePicker.setOnCountryChangeListener {
            // Update selected code when country changes
            phoneNumberInput.setText("")
        }

        // Ensure + is always at the start
        phoneNumberInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return
                validatePhoneNumber()
            }
        })
    }

    private fun validatePhoneNumber(): Boolean {
        val phoneNumber = phoneNumberInput.text.toString().trim()

        if (phoneNumber.isEmpty()) {
            phoneNumberLayout.error = "Please enter a valid phone number"
            return false
        }

        if (phoneNumber.length < 7) {
            phoneNumberLayout.error = "Please enter a valid phone number"
            return false
        }

        phoneNumberLayout.error = null
        return true
    }
}