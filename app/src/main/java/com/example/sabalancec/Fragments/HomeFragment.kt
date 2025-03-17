package com.example.sabalancec.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Data.Product
import com.example.sabalancec.ProductAdapter.ProductAdapter
import com.example.sabalancec.R


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val productList = listOf(
            Product(R.drawable.bananas, "Organic Banana", "500g", "$2.99"),
            Product(R.drawable.apple, "Red Apple", "1kg", "$3.49"),
            Product(R.drawable.walnuts, "Walnuts", "200g", "$4.99"),
            Product(R.drawable.chickpeas, "Chickpeas", "500g", "$3.29"),
            Product(R.drawable.nectarin, "Nectarine", "1kg", "$5.49"),
            Product(R.drawable.apple, "Apple", "1kg", "$3.49"),
            Product(R.drawable.garlic, "Garlic", "100g", "$2.49"),
            Product(R.drawable.pomegranate, "Pomegranate", "500g", "$6.99"),
            Product(R.drawable.cashews, "Cashews", "250g", "$7.99"),
            Product(R.drawable.chia_seeds, "Chia Seeds", "200g", "$4.59"),
            Product(R.drawable.pine_nuts, "Pine Nuts", "100g", "$6.89"),
            Product(R.drawable.brazil_nuts, "Brazil Nuts", "150g", "$5.19"),
            Product(R.drawable.dragonfruit, "Dragonfruit", "1kg", "$8.49"),
            Product(R.drawable.blueberries, "Blueberries", "150g", "$3.79"),
            Product(R.drawable.cucumber, "Cucumber", "500g", "$2.49"),
            Product(R.drawable.kiwi, "Kiwi", "4pcs", "$3.99"),
            Product(R.drawable.tomato, "Tomato", "500g", "$2.69"),
            Product(R.drawable.almonds, "Almonds", "250g", "$5.49"),
            Product(R.drawable.hazelnuts, "Hazelnuts", "200g", "$4.99"),
            Product(R.drawable.appricot, "Apricot", "500g", "$6.29"),
            Product(R.drawable.mango, "Mango", "1kg", "$4.79"),
            Product(R.drawable.bell_pepper, "Bell Pepper", "500g", "$3.49"),
            Product(R.drawable.carrot, "Carrot", "1kg", "$2.29"),
            Product(R.drawable.bananas, "Bananas", "1kg", "$2.89"),
            Product(R.drawable.potato, "Potato", "1kg", "$1.99"),
            Product(R.drawable.avocado, "Avocado", "2pcs", "$5.99"),
            Product(R.drawable.spinach, "Spinach", "200g", "$3.29"),
            Product(R.drawable.lentils, "Lentils", "500g", "$2.79"),
            Product(R.drawable.peach, "Peach", "500g", "$4.29"),
            Product(R.drawable.quinoa, "Quinoa", "500g", "$7.49"),
            Product(R.drawable.egg, "Eggs", "1 dozen", "$2.99"),
            Product(R.drawable.pineapple, "Pineapple", "1pc", "$3.59"),
            Product(R.drawable.pistachios, "Pistachios", "250g", "$8.29"),
            Product(R.drawable.strawberries, "Strawberries", "200g", "$4.99"),
            Product(R.drawable.pear, "Pear", "1kg", "$3.19")
        )


        productAdapter = ProductAdapter(productList)
        recyclerView.adapter = productAdapter

        return view
    }
}
