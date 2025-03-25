package com.example.sabalancec.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Adapter.ExpandableItemAdapter
import com.example.sabalancec.Data.Product
import com.example.sabalancec.Fragments.HomeFragment
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
            productPrice = product.price
            productDescription = product.description
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
        return product.reviews
    }

//    private fun getProductFromId(id: Int): Product? {
//        // In a real app, you would fetch this from a database or singleton data source
//        // This is a simplified example where we're recreating the list
//        // (In a production app, you'd use a ViewModel, Repository, or other data source)
//        val productList = createProductList()
//        return if (id >= 0 && id < productList.size) productList[id] else null
//    }
//
//    // Temporary function to recreate the product list - in a real app,
//    // you would use a proper data management system
//    private fun createProductList(): List<Product> {
//        // Copy the same list as in HomeFragment
//        return listOf(
//            Product(
//                R.drawable.bananas,
//                "Organic Banana",
//                "500g",
//                "$2.99",
//                "Fresh organic bananas sourced from local farms. Perfect for snacks, smoothies, or baking.",
//                mapOf(
//                    "Calories" to "89 kcal",
//                    "Protein" to "1.1g",
//                    "Carbohydrates" to "22.8g",
//                    "Fat" to "0.3g",
//                    "Fiber" to "2.6g"
//                ),
//                listOf(
//                    Review("John D.", 4.5f, "Great product! Would buy again."),
//                    Review("Mary S.", 5.0f, "Always fresh and delicious.")
//                )
//            ),
//            Product(
//                R.drawable.apple,
//                "Red Apple",
//                "1kg",
//                "$3.49",
//                "Crisp and juicy red apples. Rich in antioxidants and dietary fiber.",
//                mapOf(
//                    "Calories" to "52 kcal",
//                    "Protein" to "0.3g",
//                    "Carbohydrates" to "14g",
//                    "Fat" to "0.2g",
//                    "Fiber" to "2.4g"
//                ),
//                listOf(
//                    Review("Sarah M.", 4.0f, "Good quality apples, but a few had bruises."),
//                    Review("Michael R.", 5.0f, "Delicious and crisp!")
//                )
//            )
//            // Add more products as needed
//        )
//    }
}