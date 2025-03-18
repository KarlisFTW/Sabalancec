package com.example.sabalancec.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.R

data class Category(val name: String, val imageResId: Int)

class CategoryAdapter(
    private val context: Context,
    private var categoryList: List<Category>,
    private val clickListener: (Category) -> Unit // Added clickListener parameter
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryImage: ImageView = itemView.findViewById(R.id.categoryImage)
        val categoryLayout: LinearLayout = itemView.findViewById(R.id.categoryLayout) // reference to parent layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categories, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryName.text = category.name
        holder.categoryImage.setImageResource(category.imageResId)  // Set category image

        // Apply the dynamic background color based on the category
        val backgroundColor = getCategoryColor(category.name)
        val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.rounded_background) as GradientDrawable
        backgroundDrawable.setColor(backgroundColor)  // Set the background color while keeping the rounded shape
        holder.categoryLayout.background = backgroundDrawable

        // Set up the OnClickListener for category item
        holder.categoryLayout.setOnClickListener {
            clickListener(category)  // Trigger the clickListener with the clicked category
        }
    }

    override fun getItemCount(): Int = categoryList.size

    // Function to filter categories based on search query
    fun filter(query: String) {
        categoryList = if (query.isEmpty()) {
            listOf(
                Category("Nuts, seeds, fruit", R.drawable.pistachios),
                Category("Vegetables", R.drawable.carrot),
                Category("Greens", R.drawable.spinach),
                Category("Dairy", R.drawable.dairy),
                Category("Grains", R.drawable.lentils)
            )
        } else {
            categoryList.filter { it.name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    // Function to determine the color based on category name
    private fun getCategoryColor(categoryName: String): Int {
        return when (categoryName) {
            "Nuts, seeds, fruit" -> ContextCompat.getColor(context, R.color.category_green)
            "Vegetables" -> ContextCompat.getColor(context, R.color.category_beige)
            "Greens", "Dairy", "Grains" -> ContextCompat.getColor(context, R.color.category_red)
            else -> ContextCompat.getColor(context, R.color.category_red) // Default color if not matched
        }
    }
}
