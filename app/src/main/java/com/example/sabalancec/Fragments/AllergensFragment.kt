package com.example.sabalancec.Fragments

import android.content.Intent
import android.os.Bundle
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
import com.example.sabalancec.Activities.AllergenDetailActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllergensFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllergenAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_allergens, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recycler_allergens)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        fetchAllergens()
    }

    private fun fetchAllergens() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.warehouseApiService.getAllergens()
                val allergens = response.body()?.items

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && allergens != null) {
                        adapter = AllergenAdapter(allergens) { selectedAllergen ->
                            val intent = Intent(requireContext(), AllergenDetailActivity::class.java)
                            intent.putExtra("allergen", selectedAllergen)
                            startActivity(intent)
                        }
                        recyclerView.adapter = adapter
                    } else {
                        showToast("Failed to load allergens")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Error: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
