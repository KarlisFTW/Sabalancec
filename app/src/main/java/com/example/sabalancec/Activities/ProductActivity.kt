package com.example.sabalancec.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Adapter.ProductAdapter
import com.example.sabalancec.Products.Product
import com.example.sabalancec.Products.ProductRepository
import com.example.sabalancec.R
import kotlinx.coroutines.launch

class ProductActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productRepository: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)


        // Retrieve the category data passed from the intent
        val categoryId = intent.getStringExtra("CATEGORY_ID") ?: ""
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Unknown Category"

        // Set toolbar title
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = categoryName
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Initialize repository
        productRepository = ProductRepository()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Initialize adapter with empty list
        productAdapter = ProductAdapter(emptyList())
        recyclerView.adapter = productAdapter

        // Load products for the category
        loadProductsByCategory(categoryId)
    }

    private fun loadProductsByCategory(categoryId: String) {
        lifecycleScope.launch {
            try {
                val allProducts = productRepository.getProducts()
                val filteredProducts = allProducts.filter { product ->
                    Log.d("ProductActivity", "Product: $product")
                    product.categoryId == categoryId
                }
                productAdapter.updateProducts(filteredProducts)
            } catch (e: Exception) {
                Toast.makeText(this@ProductActivity,
                    "Error loading products: ${e.message}",
                    Toast.LENGTH_SHORT).show()
                Log.e("ProductActivity", "Error loading products", e)
            }
        }
    }
}
