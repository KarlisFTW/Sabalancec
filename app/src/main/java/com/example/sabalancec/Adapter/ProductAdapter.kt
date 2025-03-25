package com.example.sabalancec.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Activities.ProductDetails
import com.example.sabalancec.Data.Product
import com.example.sabalancec.R

class ProductAdapter(private val productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.productImage)
        val productName: TextView = view.findViewById(R.id.productName)
        val productAmount: TextView = view.findViewById(R.id.productAmount)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val addToCartButton: ImageView = view.findViewById(R.id.addToCartButton)
        val productContainer: View = view // Using the whole view as a clickable container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productImage.setImageResource(product.imageRes)
        holder.productName.text = product.name
        holder.productAmount.text = product.amount
        holder.productPrice.text = product.price

        holder.addToCartButton.setOnClickListener {
            Toast.makeText(holder.itemView.context, "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
        }

        // Set click listener for the entire product item
        holder.productContainer.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetails::class.java).apply {
                putExtra("PRODUCT", product) // Pass the entire product object
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = productList.size
}