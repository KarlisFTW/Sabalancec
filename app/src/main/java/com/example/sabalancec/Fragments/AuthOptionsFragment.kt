package com.example.sabalancec.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sabalancec.R
import com.google.android.material.button.MaterialButton

class AuthOptionsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_authoptions, container, false)

        // Set up sign in button click listener
        val signInButton = view.findViewById<MaterialButton>(R.id.btn_signin)
        signInButton.setOnClickListener {
            // Navigate to the next fragment
            navigateToNextFragment()
        }

        return view
    }

    private fun navigateToNextFragment() {
        // Replace with your target fragment
        val nextFragment = SignInFragment() // Change this to your desired fragment
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,  // enter animation
                R.anim.slide_out_left, // exit animation
                R.anim.slide_in_left,   // popEnter animation
                R.anim.slide_out_right // popExit animation
            )
            .replace(R.id.frag_authScreens, nextFragment)
            .addToBackStack(null) // Add to back stack so user can go back
            .commit()
    }
}