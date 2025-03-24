package com.example.sabalancec.Fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sabalancec.Activities.AddPhoneNumberActivity
import com.example.sabalancec.Auth.AuthManager
import com.example.sabalancec.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern


class SignUpFragment: Fragment() {

    // Field layouts
    private lateinit var nameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var streetAddressLayout: TextInputLayout
    private lateinit var cityLayout: TextInputLayout
    private lateinit var stateLayout: TextInputLayout
    private lateinit var countryLayout: TextInputLayout
    private lateinit var dobInputLayout: TextInputLayout

    // Field inputs
    private lateinit var nameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var streetAddressInput: TextInputEditText
    private lateinit var cityInput: TextInputEditText
    private lateinit var stateInput: TextInputEditText
    private lateinit var countryInput: TextInputEditText
    private lateinit var dobInput: TextInputEditText

    // Button
    private lateinit var signUpButton: MaterialButton
    private lateinit var signInLink: TextView

    // Auth
    private lateinit var authManager: AuthManager



    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        // Initialize auth manager
        authManager = AuthManager.getInstance(requireContext())

        // Initialize view references
        initViews(view)

        // Set up validation listeners
        setupValidationListeners()

        // Initialize views
        dobInputLayout = view.findViewById(R.id.txtfield_dob)
        dobInput = view.findViewById(R.id.et_dob)

        // Set up the sign in link
        signInLink = view.findViewById(R.id.tv_signin_link)
        signInLink.setOnClickListener {
            // Replace current fragment with SignInFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.frag_authScreens, SignInFragment())
                .commit()
        }

        // Set up date picker
        setupDatePicker()

//        // Set up sign in prompt click listener
//        signInPrompt.setOnClickListener {
//            // Navigate to login screen
//            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
//        }

        // Set up sign up button click listener
        signUpButton.setOnClickListener {
            if (validateAllFields()) {
                registerUser()
            }
        }

        return view
    }

    private fun setupDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        dobInput.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                // Set max date to current date (no future dates)
                datePicker.maxDate = System.currentTimeMillis()
                // You can also set a min date if needed
                // Min date for someone who is 100 years old
                val minCalendar = Calendar.getInstance()
                minCalendar.add(Calendar.YEAR, -100)
                datePicker.minDate = minCalendar.timeInMillis
                show()
            }
        }
    }

    private fun updateDateInView() {
        dobInput.setText(dateFormat.format(calendar.time))
    }

    private fun initViews(view: View) {
        // Layouts
        nameLayout = view.findViewById(R.id.txtfield_name)
        emailLayout = view.findViewById(R.id.txtfield_email)
        passwordLayout = view.findViewById(R.id.txtfield_password)
        streetAddressLayout = view.findViewById(R.id.txtfield_streetaddress)
        cityLayout = view.findViewById(R.id.txtfield_city)
        stateLayout = view.findViewById(R.id.txtfield_state)
        countryLayout = view.findViewById(R.id.txtfield_country)
        dobInputLayout = view.findViewById(R.id.txtfield_dob)

        // Inputs
        nameInput = nameLayout.editText as TextInputEditText
        emailInput = emailLayout.editText as TextInputEditText
        passwordInput = passwordLayout.editText as TextInputEditText
        streetAddressInput = streetAddressLayout.editText as TextInputEditText
        cityInput = cityLayout.editText as TextInputEditText
        stateInput = stateLayout.editText as TextInputEditText
        countryInput = countryLayout.editText as TextInputEditText
        dobInput = view.findViewById(R.id.et_dob)

        // Button
        signUpButton = view.findViewById(R.id.btn_signin)
    }

    private fun setupValidationListeners() {
        // Set up text change listeners for real-time validation
        nameInput.addTextChangedListener(createTextWatcher { validateName() })
        emailInput.addTextChangedListener(createTextWatcher { validateEmail() })
        passwordInput.addTextChangedListener(createTextWatcher { validatePassword() })
        streetAddressInput.addTextChangedListener(createTextWatcher { validateStreetAddress() })
        cityInput.addTextChangedListener(createTextWatcher { validateCity() })
        stateInput.addTextChangedListener(createTextWatcher { validateState() })
        countryInput.addTextChangedListener(createTextWatcher { validateCountry() })
        dobInput.addTextChangedListener(createTextWatcher { validateDateOfBirth() })
    }

    private fun createTextWatcher(validationFunction: () -> Boolean): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validationFunction()
            }
        }
    }

    private fun validateName(): Boolean {
        val name = nameInput.text.toString().trim()
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
        val email = emailInput.text.toString().trim()
        return when {
            email.isEmpty() -> {
                emailLayout.error = "Email is required"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailLayout.error = "Please enter a valid email address"
                false
            }
            else -> {
                emailLayout.error = null
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        val password = passwordInput.text.toString().trim()
        val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")

        return when {
            password.isEmpty() -> {
                passwordLayout.error = "Password is required"
                false
            }
            password.length < 8 -> {
                passwordLayout.error = "Password must be at least 8 characters"
                false
            }
            !passwordPattern.matcher(password).matches() -> {
                passwordLayout.error = "Password must contain at least: 1 uppercase, 1 lowercase, 1 number, and 1 special character"
                false
            }
            else -> {
                passwordLayout.error = null
                true
            }
        }
    }

    private fun validateStreetAddress(): Boolean {
        val streetAddress = streetAddressInput.text.toString().trim()
        return when {
            streetAddress.isEmpty() -> {
                streetAddressLayout.error = "Street address is required"
                false
            }
            streetAddress.length < 5 -> {
                streetAddressLayout.error = "Please enter a valid street address"
                false
            }
            else -> {
                streetAddressLayout.error = null
                true
            }
        }
    }

    private fun validateCity(): Boolean {
        val city = cityInput.text.toString().trim()
        return when {
            city.isEmpty() -> {
                cityLayout.error = "City is required"
                false
            }
            city.length < 2 -> {
                cityLayout.error = "Please enter a valid city name"
                false
            }
            else -> {
                cityLayout.error = null
                true
            }
        }
    }

    private fun validateState(): Boolean {
        val state = stateInput.text.toString().trim()
        return when {
            state.isEmpty() -> {
                stateLayout.error = "State is required"
                false
            }
            state.length < 2 -> {
                stateLayout.error = "Please enter a valid state name"
                false
            }
            else -> {
                stateLayout.error = null
                true
            }
        }
    }

    private fun validateCountry(): Boolean {
        val country = countryInput.text.toString().trim()
        return when {
            country.isEmpty() -> {
                countryLayout.error = "Country is required"
                false
            }
            country.length < 2 -> {
                countryLayout.error = "Please enter a valid country name"
                false
            }
            else -> {
                countryLayout.error = null
                true
            }
        }
    }

    private fun validateDateOfBirth(): Boolean {
        val dob = dobInput.text.toString().trim()
        if (dob.isEmpty()) {
            dobInputLayout.error = "Date of birth is required"
            return false
        }

        try {
            val dobDate = dateFormat.parse(dob)
            val dobCalendar = Calendar.getInstance()
            dobCalendar.time = dobDate

            val currentDate = Calendar.getInstance()
            val age = currentDate.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)

            if (age < 13) {
                dobInputLayout.error = "You must be at least 13 years old"
                return false
            }

            dobInputLayout.error = null
            return true
        } catch (e: Exception) {
            dobInputLayout.error = "Invalid date format"
            return false
        }
    }

    private fun validateAllFields(): Boolean {
        val nameValid = validateName()
        val emailValid = validateEmail()
        val passwordValid = validatePassword()
        val streetAddressValid = validateStreetAddress()
        val cityValid = validateCity()
        val stateValid = validateState()
        val countryValid = validateCountry()
        val dobValid = validateDateOfBirth()

        return nameValid && emailValid && passwordValid &&
                streetAddressValid && cityValid && stateValid &&
                countryValid && dobValid
    }


    private fun registerUser() {
        // Disable button and show loading state
        signUpButton.isEnabled = false
        signUpButton.text = "Signing up..."

        // Get address components
        val streetAddress = streetAddressInput.text.toString().trim()
        val city = cityInput.text.toString().trim()
        val state = stateInput.text.toString().trim()
        val country = countryInput.text.toString().trim()

        // Combine address components
        val fullAddress = "$streetAddress, $city, $state, $country"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val name = nameInput.text.toString().trim()
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                val response = authManager.register(name, email, fullAddress, password)

                withContext(Dispatchers.Main) {
                    if (response.success) {
                        Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()

                        // Launch the phone number verification activity
                        val intent = Intent(requireContext(), AddPhoneNumberActivity::class.java)
                        startActivity(intent)

                        // You might want to finish the current activity depending on your flow
                        requireActivity().finish()
                    } else {
                        // Show error message from API
                        Toast.makeText(requireContext(), response.message ?: "Registration failed", Toast.LENGTH_LONG).show()
                        // Reset button state
                        signUpButton.isEnabled = true
                        signUpButton.text = "Sign Up"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    // Reset button state
                    signUpButton.isEnabled = true
                    signUpButton.text = "Sign Up"
                }
            }
        }
    }

}