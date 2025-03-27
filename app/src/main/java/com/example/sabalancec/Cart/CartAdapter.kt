package com.example.sabalancec.Cart

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

                // Load image
                if (item.image.isNotEmpty()) {
                    Glide.with(ivCartitemImage.context)
                        .load("https://rsc97lvk0erlhrp-y8b9c67gtggi5fdi.adb.eu-zurich-1.oraclecloudapps.com/ords/warehouse/images/${item.image}")
                        .placeholder(R.drawable.carrot)
                        .error(R.drawable.carrot)
                        .into(ivCartitemImage)
                } else if (item.imageRes != 0) {
                    ivCartitemImage.setImageResource(item.imageRes)
                }

                btnIncrease.setOnClickListener { onIncrease(item) }
                btnDecrease.setOnClickListener { onDecrease(item) }
                btnDecrease2.setOnClickListener { onRemove(item) }
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