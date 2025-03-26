package com.example.sabalancec.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Adapter.AllergenAdapter
import com.example.sabalancec.AllergenApi.ApiClient
import com.example.sabalancec.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllergensFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllergenAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_allergens, container, false)
        recyclerView = view.findViewById(R.id.recycler_allergens)
        fetchAllergens()
        return view
    }

    private fun fetchAllergens() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.warehouseApiService.getAllergens()
                if (response.isSuccessful && response.body() != null) {
                    val allergens = response.body()!!.items
                    Log.d("AllergensFragment", "Fetched ${allergens.size} allergens")

                    withContext(Dispatchers.Main) {
                        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

                        adapter = AllergenAdapter(allergens)
                        recyclerView.adapter = adapter

                        Toast.makeText(requireContext(), "Loaded ${allergens.size} allergens", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Failed to load allergens", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}