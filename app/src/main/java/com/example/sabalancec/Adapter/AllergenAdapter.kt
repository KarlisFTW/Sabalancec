package com.example.sabalancec.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sabalancec.Data.Allergen
import com.example.sabalancec.R

class AllergenAdapter(
    private val items: List<Allergen>,
    private val onItemClick: (Allergen) -> Unit
) : RecyclerView.Adapter<AllergenAdapter.AllergenViewHolder>() {

    inner class AllergenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val allergenName: TextView = itemView.findViewById(R.id.txt_allergen_name)
        val seeMore: TextView = itemView.findViewById(R.id.txt_see_more)
        val allergenImage: ImageView = itemView.findViewById(R.id.img_allergen)

        fun bind(allergen: Allergen) {
            allergenName.text = allergen.name
            seeMore.text = "See more"
            allergenImage.setImageResource(R.drawable.milk) // default image

            // listener priekš visa item
            itemView.setOnClickListener {
                onItemClick(allergen)
            }

            // listener priekš seeMore tikai
            // seeMore.setOnClickListener { onItemClick(allergen) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergenViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.allergen_item, parent, false)
        return AllergenViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllergenViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
