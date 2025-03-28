package com.example.sabalancec.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.sabalancec.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class CheckoutBottomSheetFragment(
    private val totalPrice: Double,
    private val onPlaceOrder: () -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Apply rounded corners to the bottom sheet
        view.background = ContextCompat.getDrawable(requireContext(), R.drawable.bottom_sheet_rounded_corners)

        // Set the total price
        val totalPriceView = view.findViewById<TextView>(R.id.textView6)
        totalPriceView.text = "$${String.format("%.2f", totalPrice)}"

        // Close button
        view.findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            dismiss()
        }

        // Place order button
        view.findViewById<MaterialButton>(R.id.btn_save).setOnClickListener {
            onPlaceOrder()
            dismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }
}