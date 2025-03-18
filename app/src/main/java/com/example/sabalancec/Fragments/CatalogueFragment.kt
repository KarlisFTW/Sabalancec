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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Adapter.Category
import com.example.sabalancec.Adapter.CategoryAdapter
import com.example.sabalancec.Activities.ProductActivity
import com.example.sabalancec.R

class CatalogueFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
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

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewCategories)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Initialize the adapter with the click listener
        categoryAdapter = CategoryAdapter(
            requireContext(),
            categories
        ) { category ->
            // Implement the category click action here using Intent
            Log.d("CatalogueFragment", "Category clicked: ${category.name}")

            // Create an Intent to navigate to ProductActivity with the category name as extra
            val intent = Intent(requireContext(), ProductActivity::class.java)
            intent.putExtra("CATEGORY_NAME", category.name) // Pass the category name
            startActivity(intent)
        }

        recyclerView.adapter = categoryAdapter
        Log.e("CatalogueFragment", "RecyclerView initialized with ${categories.size} items")

        // Handle SearchView
        val searchView = view.findViewById<SearchView>(R.id.searchBar_catalogue)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { categoryAdapter.filter(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { categoryAdapter.filter(it) }
                return true
            }
        })

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = "Find Products" // Set the title for the toolbar

        return view
    }
}
