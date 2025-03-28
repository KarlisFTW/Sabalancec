package com.example.sabalancec.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.R
import com.example.sabalancec.models.ExpandableItem
import com.example.sabalancec.models.Review

class ExpandableItemAdapter(
    private val context: Context,
    private val items: List<ExpandableItem>
) : RecyclerView.Adapter<ExpandableItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerLayout: LinearLayout = itemView.findViewById(R.id.header_layout)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_title)
        val arrowImageView: ImageView = itemView.findViewById(R.id.iv_arrow)
        val contentLayout: LinearLayout = itemView.findViewById(R.id.content_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_expandable, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Set the title
        holder.titleTextView.text = item.title

        // Set the arrow direction based on expanded state
        holder.arrowImageView.setImageResource(
            if (item.isExpanded) R.drawable.ic_arrow_down else R.drawable.ic_arrow_right
        )

        // Set content visibility based on expanded state
        holder.contentLayout.visibility = if (item.isExpanded) View.VISIBLE else View.GONE

        // Always refresh content when expanded to prevent incorrect content display
        if (item.isExpanded) {
            // Clear any existing content
            holder.contentLayout.removeAllViews()

            // Inflate proper content view
            val contentView = LayoutInflater.from(context).inflate(item.contentLayout, holder.contentLayout, false)
            holder.contentLayout.addView(contentView)

            // Bind the correct data
            when (item.title) {
                "Product Details" -> bindProductDetailsContent(contentView, item.contentViewData)
                "Nutrition" -> bindNutritionContent(contentView, item.contentViewData)
                "Reviews" -> bindReviewsContent(contentView, item.contentViewData)
            }
        }

        // Set click listener for header
        holder.headerLayout.setOnClickListener {
            // Toggle expanded state
            item.isExpanded = !item.isExpanded
            notifyItemChanged(position)
        }
    }

    private fun bindProductDetailsContent(view: View, data: Any?) {
        val description = data as? String ?: "No description available"
        view.findViewById<TextView>(R.id.tv_description)?.text = description
    }

    @Suppress("UNCHECKED_CAST")
    private fun bindNutritionContent(view: View, data: Any?) {
        val nutritionData = data as? Map<String, String> ?: return

        // Get the table layout to add rows dynamically
        val tableLayout = view.findViewById<TableLayout>(R.id.nutrition_table)
        tableLayout?.removeAllViews() // Clear existing rows

        // Add rows for each nutrition value
        for ((nutrient, value) in nutritionData) {
            val row = TableRow(context)

            val nutrientTextView = TextView(context).apply {
                text = nutrient
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
            }

            val valueTextView = TextView(context).apply {
                text = value
            }

            row.addView(nutrientTextView)
            row.addView(valueTextView)

            tableLayout?.addView(row)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun bindReviewsContent(view: View, data: Any?) {
        val reviews = data as? List<Review> ?: return

        // Get the container for reviews
        val reviewsContainer = view.findViewById<LinearLayout>(R.id.reviews_container)
        reviewsContainer?.removeAllViews() // Clear existing reviews

        // Calculate average rating
        var totalRating = 0f
        reviews.forEach { totalRating += it.rating }
        val avgRating = if (reviews.isNotEmpty()) totalRating / reviews.size else 0f

        // Set average rating
        view.findViewById<RatingBar>(R.id.rating_bar)?.rating = avgRating

        // Set rating text
        view.findViewById<TextView>(R.id.rating_text)?.text =
            String.format(reviews.size.toString() + " ratings")

        // Add individual reviews
        for (review in reviews) {
            val reviewView = LayoutInflater.from(context).inflate(R.layout.item_review, reviewsContainer, false)

            reviewView.findViewById<TextView>(R.id.reviewer_name)?.text = review.userName
            reviewView.findViewById<RatingBar>(R.id.review_rating)?.rating = review.rating
            reviewView.findViewById<TextView>(R.id.review_comment)?.text = review.comment

            if (review.date.isNotEmpty()) {
                reviewView.findViewById<TextView>(R.id.review_date)?.apply {
                    visibility = View.VISIBLE
                    text = review.date
                }
            }

            reviewsContainer?.addView(reviewView)
        }
    }
}