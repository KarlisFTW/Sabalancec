package com.example.sabalancec.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.R

class HomeCategoryAdapter(
    private val context: Context,
    private var categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<HomeCategoryAdapter.HomeCategoryViewHolder>() {

    class HomeCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage: ImageView = itemView.findViewById(R.id.iv_category)
        val categoryName: TextView = itemView.findViewById(R.id.tv_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_home, parent, false)
        return HomeCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeCategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name

        // Try to load image from drawable based on category name
        val categoryNameLower = category.name.lowercase() // Get first word like "nuts"
        val resourceId = holder.itemView.context.resources.getIdentifier(
            categoryNameLower, "drawable", holder.itemView.context.packageName
        )

        if (resourceId != 0) {
            // Drawable resource found
            holder.categoryImage.setImageResource(resourceId)
        } else {
            // Use provided imageResId as fallback
            holder.categoryImage.setImageResource(category.imageResId)
        }

        holder.itemView.setOnClickListener {
            onCategoryClick(category)
        }
    }

    override fun getItemCount() = categories.size

    fun updateCategories(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}