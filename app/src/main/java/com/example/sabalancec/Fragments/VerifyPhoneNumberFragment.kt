package com.example.sabalancec.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sabalancec.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class VerifyPhoneNumberFragment : Fragment() {

    private lateinit var codeInput: TextInputEditText
    private lateinit var codeInputLayout: TextInputLayout
    private lateinit var continueButton: FloatingActionButton
    private lateinit var resendCodeButton: TextView
    private var phoneNumber: String? = null

    // Add this callback variable
    private var onVerificationCompleteListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phoneNumber = it.getString("phone_number")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_verify_phone_nr, container, false)

        // Initialize views
        codeInput = view.findViewById(R.id.code)
        codeInputLayout = view.findViewById(R.id.txtfield_code)
        continueButton = view.findViewById(R.id.btn_continue)
        resendCodeButton = view.findViewById(R.id.txt_skip)

        setupCodeInput()

        // For demonstration, we're just allowing the user to continue without validation
        continueButton.setOnClickListener {
            // Call the verification complete listener
            onVerificationCompleteListener?.invoke()
        }

        resendCodeButton.setOnClickListener {
            // In a real app, you would request a new code here
        }

        return view
    }

    // Add this method to allow setting the callback
    fun setOnVerificationCompleteListener(listener: () -> Unit) {
        onVerificationCompleteListener = listener
    }

    private fun setupCodeInput() {
        codeInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateCode()
            }
        })
    }

    private fun validateCode(): Boolean {
        val code = codeInput.text.toString().trim()

        if (code.length != 4) {
            codeInputLayout.error = "Please enter a 4-digit code"
            return false
        }

        codeInputLayout.error = null
        return true
    }
}