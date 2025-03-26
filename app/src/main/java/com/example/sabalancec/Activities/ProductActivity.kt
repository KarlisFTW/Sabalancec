package com.example.sabalancec.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Adapter.ProductAdapter
import com.example.sabalancec.Products.Product
import com.example.sabalancec.R

class ProductActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private var productList: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        // Retrieve the category name passed from the intent
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Unknown Category"

        // Set the toolbar title to the category name
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = categoryName
        setSupportActionBar(toolbar)  // Ensure the toolbar is used as the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Show back arrow

        // Handle the back button click
        toolbar.setNavigationOnClickListener {
            finish()  // Handle back navigation when the icon is clicked
        }
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = GridLayoutManager(this, 2)  // Grid layout with 2 columns

        // Get the products for the selected category
        //productList = getProductsByCategory(categoryName)

        // Set up the adapter for the RecyclerView
        productAdapter = ProductAdapter(productList)
        recyclerView.adapter = productAdapter
    }

//    private fun getProductsByCategory(categoryName: String?): List<Product> {
//        // Replace this with actual logic to fetch products based on the category
//        return when (categoryName) {
//            "Nuts, seeds, fruit" -> listOf(
//                Product(R.drawable.almonds, "Almonds", "100g", "$5.99"),
//                Product(R.drawable.cashews, "Cashews", "100g", "$6.49")
//            )
//            "Vegetables" -> listOf(
//                Product(R.drawable.carrot, "Carrot", "500g", "$2.99"),
//                Product(R.drawable.spinach, "Spinach", "300g", "$3.49")
//            )
//            "Greens" -> listOf(
//                Product(R.drawable.spinach, "Spinach", "300g", "$3.49"),
//                Product(R.drawable.kale, "Kale", "200g", "$4.99")
//            )
//            "Dairy" -> listOf(
//                Product(R.drawable.milk, "Milk", "1L", "$1.49"),
//                Product(R.drawable.cheese, "Cheese", "200g", "$2.99")
//            )
//            "Grains" -> listOf(
//                Product(R.drawable.lentils, "Lentils", "500g", "$3.99"),
//                Product(R.drawable.rice, "Rice", "1kg", "$2.49")
//            )
//            else -> emptyList()
//        }
//    }
}
