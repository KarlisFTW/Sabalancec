package com.example.sabalancec.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Adapter.ProductAdapter
import com.example.sabalancec.Products.Product
import com.example.sabalancec.R
import com.example.sabalancec.Products.ProductRepository
import kotlinx.coroutines.launch
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sabalancec.Activities.ProductActivity
import com.example.sabalancec.Adapter.Category
import com.example.sabalancec.Adapter.HomeCategoryAdapter

import com.example.sabalancec.Products.ProductCache

class HomeFragment : Fragment() {
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productRepository: ProductRepository
    private var isDataLoaded = false
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var bestsellerAdapter: ProductAdapter
    private val categories = mutableListOf<Category>()
    private lateinit var homeCategoryAdapter: HomeCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        productRepository = ProductRepository()
        setupRecyclerView(view)
        setupSwipeRefresh(view)
        loadCategories()

        return view
    }

    private fun setupSwipeRefresh(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            // Clear cache and reload products with forceRefresh set to true
            ProductCache.clearCache()
            loadProducts(true)
        }
        swipeRefreshLayout.setColorSchemeResources(
            R.color.lime_green,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light
        )
    }

    private fun loadProducts(forceRefresh: Boolean = false) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val products = productRepository.getProducts(forceRefresh)

                // Update both adapters with the same data
                productAdapter.updateProducts(products)
                bestsellerAdapter.updateProducts(products)

                isDataLoaded = true
            } catch (e: Exception) {
                Toast.makeText(context, "Error loading products: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("HomeFragment", "Error loading products", e)
            } finally {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isDataLoaded) {
            loadProducts()
        }
    }

    private fun setupRecyclerView(view: View) {
        // Set up Categories recycler
        val categoriesRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_Categories)
        categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        homeCategoryAdapter = HomeCategoryAdapter(requireContext(), categories) { category ->
            // Open ProductActivity when category is clicked
            val intent = Intent(requireContext(), ProductActivity::class.java)
            intent.putExtra("CATEGORY_ID", category.name)
            intent.putExtra("CATEGORY_NAME", category.name)
            startActivity(intent)
        }
        categoriesRecyclerView.adapter = homeCategoryAdapter

        // Existing product recyclers setup
        val recyclerView = view.findViewById<RecyclerView>(R.id.productRecyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        productAdapter = ProductAdapter(emptyList())
        recyclerView.adapter = productAdapter

        val bestsellerRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_bestsellers)
        bestsellerRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        bestsellerAdapter = ProductAdapter(emptyList())
        bestsellerRecyclerView.adapter = bestsellerAdapter
    }


    private fun updateAdapterData(adapter: ProductAdapter, products: List<Product>) {
        // Add a method to your ProductAdapter class to update data
        if (adapter is ProductAdapter) {
            adapter.updateProducts(products)
        }
    }

    // Add this method to set up and load categories
    private fun loadCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val categoryResponse = productRepository.getCategories()

                val apiCategories = categoryResponse.items.map { item ->
                    Category(
                        name = item.category,
                        imageResId = getCategoryImage(item.category)
                    )
                }

                categories.clear()
                categories.addAll(apiCategories)
                homeCategoryAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error loading categories: ${e.message}", e)
                loadFallbackCategories()
            }
        }
    }

    private fun getCategoryImage(categoryName: String): Int {
        return when {
            categoryName.contains("Nuts", ignoreCase = true) -> R.drawable.pistachios
            categoryName.contains("Vegetable", ignoreCase = true) -> R.drawable.carrot
            categoryName.contains("Green", ignoreCase = true) -> R.drawable.spinach
            categoryName.contains("Dairy", ignoreCase = true) -> R.drawable.dairy
            categoryName.contains("Grain", ignoreCase = true) -> R.drawable.lentils
            else -> R.drawable.carrot // Default image
        }
    }

    private fun loadFallbackCategories() {
        categories.clear()
        categories.addAll(listOf(
            Category("Nuts, seeds, fruit", R.drawable.pistachios),
            Category("Vegetables", R.drawable.carrot),
            Category("Greens", R.drawable.spinach),
            Category("Dairy", R.drawable.dairy),
            Category("Grains", R.drawable.lentils)
        ))
        homeCategoryAdapter.notifyDataSetChanged()
    }


//    private fun loadHardcodedProducts() {
//        // Your existing hardcoded products as fallback
//        val hardcodedProducts = listOf(

//        )
//        productAdapter = ProductAdapter(hardcodedProducts)
//        view?.findViewById<RecyclerView>(R.id.productRecyclerView)?.adapter = productAdapter
//    }
}