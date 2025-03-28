package com.example.sabalancec.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

class OrdersBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ordersAdapter: OrderItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Apply rounded corners
        view.background = ContextCompat.getDrawable(requireContext(), R.drawable.bottom_sheet_rounded_corners)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_orders)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Generate random order items
        val items = generateRandomOrderItems(10)

        // Set up the adapter
        ordersAdapter = OrderItemAdapter(items)
        recyclerView.adapter = ordersAdapter

        // Close button
        val closeButton = view.findViewById<ImageButton>(R.id.imageButton)
        closeButton.setOnClickListener { dismiss() }

        // Save button (in this case used as "Close" button)
        val saveButton = view.findViewById<MaterialButton>(R.id.btn_save)
        saveButton.text = "Close"
        saveButton.setOnClickListener { dismiss() }
    }

    private fun generateRandomOrderItems(count: Int): List<OrderItem> {
        val products = listOf(
            "Organic Banana" to "500g",
            "Fresh Strawberries" to "300g",
            "Bell Pepper" to "1kg",
            "Ginger" to "250g",
            "Dairy Milk" to "1L",
            "Organic Apples" to "1kg",
            "Fresh Broccoli" to "500g",
            "Chicken Breasts" to "500g",
            "Atlantic Salmon" to "300g",
            "Whole Grain Bread" to "750g",
            "Greek Yogurt" to "500g",
            "Black Beans" to "400g"
        )

        return List(count) { index ->
            val product = products.random()
            val price = (Random.nextDouble(1.0, 15.0) * 100).toInt() / 100.0
            val qty = Random.nextInt(1, 5)

            OrderItem(
                id = index.toString(),
                name = product.first,
                amount = product.second,
                price = price,
                quantity = qty,
                imageResId = R.drawable.bell_pepper // Using a default image
            )
        }
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    // Data class for order items
    data class OrderItem(
        val id: String,
        val name: String,
        val amount: String,
        val price: Double,
        val quantity: Int,
        val imageResId: Int
    )

    // Adapter for the RecyclerView
    inner class OrderItemAdapter(private val items: List<OrderItem>) :
        RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameText: TextView = view.findViewById(R.id.tv_orderitem_name)
            val amountText: TextView = view.findViewById(R.id.tv_priceper)
            val priceText: TextView = view.findViewById(R.id.tv_orderitem_price)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_orderitem, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            val format = NumberFormat.getCurrencyInstance(Locale.US)

            holder.nameText.text = item.name
            holder.amountText.text = "${item.amount}, ${format.format(item.price)} × ${item.quantity}"
            holder.priceText.text = format.format(item.price * item.quantity)

            // Hide delete button as it's a past order
            val deleteButton = holder.itemView.findViewById<ImageButton>(R.id.btn_delete)
            deleteButton.visibility = View.GONE
        }

        override fun getItemCount() = items.size
    }
}