package com.example.sabalancec.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sabalancec.Activities.ProductDetails
import com.example.sabalancec.Cart.AppDatabase
import com.example.sabalancec.Products.Product
import com.example.sabalancec.R
import com.example.sabalancec.Cart.CartViewModel
import com.example.sabalancec.Cart.CartViewModelFactory
import com.example.sabalancec.Cart.CartRepository

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
            val imageName = product.image.substringBeforeLast(".") // Remove file extension
            Log.d("ProductAdapter", "Image name: $imageName")
            val resourceId = holder.itemView.context.resources.getIdentifier(
                imageName, "drawable", holder.itemView.context.packageName
            )

            if (resourceId != 0) {
                // Drawable resource found
                holder.productImage.setImageResource(resourceId)
            } else {
                // Drawable resource not found, use fallback
                holder.productImage.setImageResource(R.drawable.carrot)
            }
        } else if (product.imageRes != 0) {
            // Use imageRes as fallback if available
            holder.productImage.setImageResource(product.imageRes)
        } else {
            // Default fallback image
            holder.productImage.setImageResource(R.drawable.carrot)
        }

        holder.productName.text = product.name
        holder.productAmount.text = product.amount
        holder.productPrice.text = "$${String.format("%.2f", product.price)}"

        val cartDao = AppDatabase.getDatabase(holder.itemView.context).cartDao()
        val cartViewModel = CartViewModelFactory(CartRepository(cartDao)).create(CartViewModel::class.java)
        holder.addToCartButton.setOnClickListener {
            cartViewModel.addOrIncreaseProduct(product, 1)
            val message = if (cartViewModel.isProductInCart(product.id).value == true) {
                "${product.name} quantity increased!"
            } else {
                "${product.name} added to cart!"
            }
            Toast.makeText(holder.itemView.context, message, Toast.LENGTH_SHORT).show()
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