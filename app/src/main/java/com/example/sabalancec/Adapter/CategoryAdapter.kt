package com.example.sabalancec.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.R

data class Category(val name: String, val imageResId: Int)

class CategoryAdapter(private var categoryList: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryImage: ImageView = itemView.findViewById(R.id.categoryImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categories, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryName.text = category.name
        holder.categoryImage.setImageResource(category.imageResId)
    }

    override fun getItemCount(): Int = categoryList.size

    fun filter(query: String) {
        val originalList = listOf(
            Category("Nuts, seeds, fruit", R.drawable.pistachios),
            Category("Vegetables", R.drawable.carrot),
            Category("Greens", R.drawable.spinach),
            Category("Dairy", R.drawable.dairy),
            Category("Grains", R.drawable.lentils)
        )

        categoryList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter { it.name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}
