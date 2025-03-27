package com.example.sabalancec.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sabalancec.Activities.ProductDetails
import com.example.sabalancec.Cart.AppDatabase
import com.example.sabalancec.Products.Product
import com.example.sabalancec.R
import com.example.sabalancec.Cart.CartViewModel
import com.example.sabalancec.Cart.CartViewModelFactory
import com.example.sabalancec.Cart.CartRepository
import com.example.sabalancec.Cart.CartDao

class ProductAdapter(private var productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

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

        // Load image from URL using Glide or similar library
        if (product.image.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load("https://rsc97lvk0erlhrp-y8b9c67gtggi5fdi.adb.eu-zurich-1.oraclecloudapps.com/ords/warehouse/images/" + product.image)
                .placeholder(R.drawable.carrot)
                .error(R.drawable.carrot)
                .into(holder.productImage)
        } else {
            // Use resource ID as fallback
            holder.productImage.setImageResource(product.imageRes)
        }

        holder.productImage.setImageResource(product.imageRes)
        holder.productName.text = product.name
        holder.productAmount.text = product.amount
        holder.productPrice.text = "$"+product.price.toString()

        val cartDao = AppDatabase.getDatabase(holder.itemView.context).cartDao()
        val cartViewModel = CartViewModelFactory(CartRepository(cartDao)).create(CartViewModel::class.java)
        holder.addToCartButton.setOnClickListener {
            cartViewModel.addToCart(product)
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

    fun updateProducts(newProducts: List<Product>) {
        this.productList = newProducts
        notifyDataSetChanged()
    }

    override fun getItemCount() = productList.size
}