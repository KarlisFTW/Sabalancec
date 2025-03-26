package com.example.sabalancec.Fragments

import android.os.Bundle
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
import com.example.sabalancec.Products.ProductCache

class HomeFragment : Fragment() {
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productRepository: ProductRepository
    private var isDataLoaded = false
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        productRepository = ProductRepository()
        setupRecyclerView(view)
        setupSwipeRefresh(view)

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
                (view?.findViewById<RecyclerView>(R.id.productRecyclerView)?.adapter as? ProductAdapter)?.
                let { adapter ->
                    updateAdapterData(adapter, products)
                }
                isDataLoaded = true
            } catch (e: Exception) {
                Toast.makeText(context, "Error loading products: ${e.message}", Toast.LENGTH_LONG).show()
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
        val recyclerView = view.findViewById<RecyclerView>(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        productAdapter = ProductAdapter(emptyList()) // Start with empty list
        recyclerView.adapter = productAdapter
    }


    private fun updateAdapterData(adapter: ProductAdapter, products: List<Product>) {
        // Add a method to your ProductAdapter class to update data
        if (adapter is ProductAdapter) {
            adapter.updateProducts(products)
        }
    }


//    private fun loadHardcodedProducts() {
//        // Your existing hardcoded products as fallback
//        val hardcodedProducts = listOf(

//        )
//        productAdapter = ProductAdapter(hardcodedProducts)
//        view?.findViewById<RecyclerView>(R.id.productRecyclerView)?.adapter = productAdapter
//    }
}