package com.example.sabalancec.Activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Adapter.ExpandableItemAdapter
import com.example.sabalancec.Products.Product
import com.example.sabalancec.R
import com.example.sabalancec.models.ExpandableItem
import android.widget.ImageView
import android.widget.TextView
import com.example.sabalancec.models.Review

class ProductDetails : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpandableItemAdapter
    private lateinit var product: Product

    // References to product data from intent
    private var productImage: Int = 0
    private var productName: String = ""
    private var productAmount: String = ""
    private var productPrice: String = ""
    private var productDescription: String = ""
    private var productId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Extract product data from intent
        extractProductData()

        // Display basic product info
        setupProductHeader()

        // Setup RecyclerView with expandable items
        setupRecyclerView()
    }

    private fun extractProductData() {
        intent.extras?.let {
            product = it.getParcelable("PRODUCT")!!

            // For backward compatibility, also extract individual fields
            productImage = product.imageRes
            productName = product.name
            productAmount = product.amount
            productPrice = "$"+product.price
            //if description is empty, set it to a default value
            productDescription = product.description.ifEmpty { "No description available for $productName" }
        }
    }

    private fun setupProductHeader() {
        // Set product image, name, amount and price at the top of the details screen
        findViewById<ImageView>(R.id.iv_product_image)?.setImageResource(productImage)
        findViewById<TextView>(R.id.tv_productname)?.text = productName
        //findViewById<TextView>(R.id.product_amount)?.text = productAmount
        findViewById<TextView>(R.id.tv_total_price)?.text = productPrice
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = listOf(
            ExpandableItem(
                "Product Details",
                R.layout.layout_product_details_content,
                false,
                productDescription
            ),
            ExpandableItem(
                "Nutrition",
                R.layout.layout_nutrition_content,
                false,
                getNutritionData()
            ),
            ExpandableItem(
                "Reviews",
                R.layout.layout_reviews_content,
                false,
                getReviewsData()
            )
        )

        adapter = ExpandableItemAdapter(this, items)
        recyclerView.adapter = adapter
    }

    private fun getNutritionData(): Map<String, String> {
        return product.nutritionValues
    }

    private fun getReviewsData(): List<Review> {
        Log.d("ProductDetails", "Product category: ${product.categoryName}")
        Log.d("ProductDetails", "Product Reviews: ${product.reviews}")
        return product.reviews
    }
}