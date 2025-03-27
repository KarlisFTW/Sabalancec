package com.example.sabalancec.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Adapter.Category
import com.example.sabalancec.Adapter.CategoryAdapter
import com.example.sabalancec.Adapter.ProductAdapter
import com.example.sabalancec.Activities.ProductActivity
import com.example.sabalancec.Activities.ProductDetails
import com.example.sabalancec.Products.ProductRepository
import com.example.sabalancec.R
import kotlinx.coroutines.launch

class CatalogueFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productRepository: ProductRepository
    private var isSearchActive = false

    private val categories = listOf(
        Category("Nuts, seeds, fruit", R.drawable.pistachios),
        Category("Vegetables", R.drawable.carrot),
        Category("Greens", R.drawable.spinach),
        Category("Dairy", R.drawable.dairy),
        Category("Grains", R.drawable.lentils)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalogue, container, false)

        // Initialize repository
        productRepository = ProductRepository()

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewCategories)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Initialize the adapters
        categoryAdapter = CategoryAdapter(
            requireContext(),
            categories
        ) { category ->
            Log.d("CatalogueFragment", "Category clicked: ${category.name}")
            val intent = Intent(requireContext(), ProductActivity::class.java)
            intent.putExtra("CATEGORY_NAME", category.name)
            startActivity(intent)
        }

        productAdapter = ProductAdapter(emptyList())

        // Start with categories
        recyclerView.adapter = categoryAdapter
        Log.e("CatalogueFragment", "RecyclerView initialized with ${categories.size} items")

        // Handle SearchView
        val searchView = view.findViewById<SearchView>(R.id.searchBar_catalogue)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchProducts(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty() && isSearchActive) {
                    // Reset to category view
                    showCategories()
                } else if (!newText.isNullOrEmpty() && newText.length > 2) {
                    // Start searching after typing 3 characters
                    searchProducts(newText)
                }
                return true
            }
        })

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = "Find Products"

        return view
    }

    private fun searchProducts(query: String) {
        isSearchActive = true
        lifecycleScope.launch {
            try {
                val allProducts = productRepository.getProducts()
                val filteredProducts = allProducts.filter { product ->
                    product.name.contains(query, ignoreCase = true)
                }

                recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                recyclerView.adapter = productAdapter
                productAdapter.updateProducts(filteredProducts)
            } catch (e: Exception) {
                Log.e("CatalogueFragment", "Error searching products: ${e.message}", e)
            }
        }
    }

    private fun showCategories() {
        isSearchActive = false
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = categoryAdapter
    }
}