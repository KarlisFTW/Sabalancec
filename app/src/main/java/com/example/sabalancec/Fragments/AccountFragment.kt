package com.example.sabalancec.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sabalancec.Auth.AuthManager
import com.example.sabalancec.R
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.sabalancec.Activities.WelcomeActivity

class AccountFragment : Fragment() {

    private lateinit var authManager: AuthManager
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var logoutButton: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        authManager = AuthManager.getInstance(requireContext())

        usernameTextView = view.findViewById(R.id.txt_username)
        emailTextView = view.findViewById(R.id.txt_useremail)
        profileImageView = view.findViewById(R.id.img_profile)
        logoutButton = view.findViewById(R.id.btn_logout)

        val rowOrders = view.findViewById<View>(R.id.row_orders)
        val rowDetails = view.findViewById<View>(R.id.row_details)
        val rowAddress = view.findViewById<View>(R.id.row_address)

        rowOrders.findViewById<TextView>(R.id.row_title).text = "Orders"
        rowOrders.findViewById<ImageView>(R.id.row_icon).setImageResource(R.drawable.ic_orders)

        rowDetails.findViewById<TextView>(R.id.row_title).text = "My Details"
        rowDetails.findViewById<ImageView>(R.id.row_icon).setImageResource(R.drawable.ic_user)

        rowAddress.findViewById<TextView>(R.id.row_title).text = "Delivery Address"
        rowAddress.findViewById<ImageView>(R.id.row_icon).setImageResource(R.drawable.ic_location)

        displayUserInfo()

        rowOrders.setOnClickListener {
            OrdersBottomSheetFragment().show(childFragmentManager, "OrdersBottomSheet")
        }

        rowDetails.setOnClickListener {
            MyDetailsBottomSheetFragment().show(childFragmentManager, "MyDetailsBottomSheet")
        }

        rowAddress.setOnClickListener {
            DeliveryAddressBottomSheetFragment().show(childFragmentManager, "DeliveryAddressBottomSheet")
        }

        logoutButton.setOnClickListener {
            Toast.makeText(requireContext(), "Logging out...", Toast.LENGTH_SHORT).show()

            // Launch coroutine to handle logout (since logout is a suspend function)
            viewLifecycleOwner.lifecycleScope.launch {
                if (authManager.logout()) {
                    Toast.makeText(requireContext(), "Logout successful", Toast.LENGTH_SHORT).show()

                    // Navigate back to welcome screen
                    val welcomeIntent = Intent(requireContext(), WelcomeActivity::class.java)
                    welcomeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(welcomeIntent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Logout failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun displayUserInfo() {
        val name = authManager.getUserFullName() ?: "User Name"
        val email = authManager.getUserEmail() ?: "email@example.com"

        usernameTextView.text = name
        emailTextView.text = email
        // profileImageView.setImageResource(...) // optional: set a drawable if needed
    }
}
