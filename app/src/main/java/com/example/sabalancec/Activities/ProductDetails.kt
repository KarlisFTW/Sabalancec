package com.example.sabalancec.Activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Adapter.ExpandableItemAdapter
import com.example.sabalancec.Cart.AppDatabase
import com.example.sabalancec.Cart.CartRepository
import com.example.sabalancec.Cart.CartViewModel
import com.example.sabalancec.Cart.CartViewModelFactory
import com.example.sabalancec.Products.Product
import com.example.sabalancec.R
import com.example.sabalancec.models.ExpandableItem
import com.example.sabalancec.models.Review
import com.google.android.material.button.MaterialButton

class ProductDetails : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpandableItemAdapter
    private lateinit var product: Product
    private var quantity = 1 // Default quantity

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

        // Setup quantity counter
        setupQuantityCounter()

        // Setup add to cart button
        setupAddToCartButton()

        // Setup RecyclerView with expandable items
        setupRecyclerView()
    }

    private fun setupQuantityCounter() {
        val counterView = findViewById<TextView>(R.id.tv_counter)
        val increaseButton = findViewById<ImageButton>(R.id.btn_increase)
        val decreaseButton = findViewById<ImageButton>(R.id.btn_decrease)
        val totalPriceView = findViewById<TextView>(R.id.tv_total_price)

        counterView.text = quantity.toString()
        updateTotalPrice()
        updateDecreaseButtonColor() // Initialize button color

        increaseButton.setOnClickListener {
            quantity++
            counterView.text = quantity.toString()
            updateTotalPrice()
            updateDecreaseButtonColor() // Update color after increasing
        }

        decreaseButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                counterView.text = quantity.toString()
                updateTotalPrice()
                updateDecreaseButtonColor() // Update color after decreasing
            }
        }
    }

    private fun updateDecreaseButtonColor() {
        val decreaseButton = findViewById<ImageButton>(R.id.btn_decrease)
        val buttonColor = if (quantity <= 1) {
            getColor(R.color.black)
        } else {
            getColor(R.color.olive_green)
        }
        decreaseButton.setColorFilter(buttonColor)
    }

    private fun updateTotalPrice() {
        val totalPriceView = findViewById<TextView>(R.id.tv_total_price)
        val unitPrice = product.price
        val total = unitPrice * quantity
        totalPriceView.text = "$${String.format("%.2f", total)}"
    }

    private fun setupAddToCartButton() {
        val addToCartButton = findViewById<MaterialButton>(R.id.btn_addtocart)

        addToCartButton.setOnClickListener {
            val cartDao = AppDatabase.getDatabase(this).cartDao()
            val cartRepository = CartRepository(cartDao)
            val cartViewModel = CartViewModelFactory(cartRepository).create(CartViewModel::class.java)

            cartViewModel.addOrIncreaseProduct(product, quantity)

            Toast.makeText(this, "$quantity ${product.name} added to cart!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun extractProductData() {
        intent.extras?.let {
            product = it.getParcelable("PRODUCT")!!

            // For backward compatibility, also extract individual fields
            productImage = product.imageRes
            productName = product.name
            productAmount = product.amount
            productPrice = "$"+product.price
            productDescription = product.description.ifEmpty { "No description available for $productName" }
        }
    }

    private fun setupProductHeader() {
        // Try to load image from drawable resources
        if (product.image.isNotEmpty()) {
            val imageName = product.image.substringBeforeLast(".") // Remove file extension
            val resourceId = resources.getIdentifier(
                imageName, "drawable", packageName
            )

            if (resourceId != 0) {
                // Drawable resource found
                findViewById<ImageView>(R.id.iv_product_image)?.setImageResource(resourceId)
            } else if (product.imageRes != 0) {
                // Use imageRes as fallback
                findViewById<ImageView>(R.id.iv_product_image)?.setImageResource(product.imageRes)
            } else {
                // Default fallback image
                findViewById<ImageView>(R.id.iv_product_image)?.setImageResource(R.drawable.carrot)
            }
        } else {
            // No image string, use imageRes or default
            findViewById<ImageView>(R.id.iv_product_image)?.setImageResource(
                if (product.imageRes != 0) product.imageRes else R.drawable.carrot
            )
        }

        findViewById<TextView>(R.id.tv_productname)?.text = productName
        findViewById<TextView>(R.id.tv_priceper)?.text = productAmount
        findViewById<TextView>(R.id.tv_total_price)?.text = productPrice
    }

    private fun setupRecyclerView() {
        // Existing implementation...
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
        return product.reviews
    }
}