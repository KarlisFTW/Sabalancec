package com.example.sabalancec.Cart

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sabalancec.R
import com.example.sabalancec.databinding.ItemCartitemBinding

class CartAdapter(
    private val onIncrease: (CartItem) -> Unit,
    private val onDecrease: (CartItem) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    class CartViewHolder(private val binding: ItemCartitemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CartItem,
            onIncrease: (CartItem) -> Unit,
            onDecrease: (CartItem) -> Unit,
            onRemove: (CartItem) -> Unit
        ) {
            binding.apply {
                tvCartitemName.text = item.name
                tvPriceper.text = item.amount
                tvCounter.text = item.quantity.toString()
                tvCartitemPrice.text = "$${String.format("%.2f", item.getTotalPrice())}"

                // Set decrease button color based on quantity
                val decreaseButtonColor = if (item.quantity <= 1) {
                    root.context.getColor(R.color.black)
                } else {
                    root.context.getColor(R.color.olive_green)
                }

                // Apply the color to the decrease button icon
                btnDecrease.setColorFilter(decreaseButtonColor)


                // Load image from drawable resources
                if (item.image.isNotEmpty()) {
                    val imageName = item.image.substringBeforeLast(".") // Remove file extension
                    val resourceId = root.context.resources.getIdentifier(
                        imageName, "drawable", root.context.packageName
                    )

                    if (resourceId != 0) {
                        // Drawable resource found
                        ivCartitemImage.setImageResource(resourceId)
                    } else {
                        // Drawable resource not found, use fallback
                        ivCartitemImage.setImageResource(R.drawable.carrot)
                    }
                } else if (item.imageRes != 0) {
                    // Use imageRes as fallback if available
                    ivCartitemImage.setImageResource(item.imageRes)
                } else {
                    // Default fallback image
                    ivCartitemImage.setImageResource(R.drawable.carrot)
                }

                btnIncrease.setOnClickListener { onIncrease(item) }
                btnDecrease.setOnClickListener { onDecrease(item) }
                btnDelete.setOnClickListener { onRemove(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position), onIncrease, onDecrease, onRemove)
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}