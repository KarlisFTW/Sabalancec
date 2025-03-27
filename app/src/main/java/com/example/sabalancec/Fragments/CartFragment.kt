package com.example.sabalancec.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sabalancec.Cart.AppDatabase
import com.example.sabalancec.Cart.CartAdapter
import com.example.sabalancec.Cart.CartRepository
import com.example.sabalancec.Cart.CartViewModel
import com.example.sabalancec.Cart.CartViewModelFactory
import com.example.sabalancec.R
import com.example.sabalancec.databinding.FragmentCartBinding


class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartRepository: CartRepository
    private lateinit var cartAdapter: CartAdapter

    private val viewModel: CartViewModel by viewModels {
        val cartDao = AppDatabase.getDatabase(requireContext()).cartDao()
        CartViewModelFactory(CartRepository(cartDao))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeCartData()

        binding.btnCheckout.setOnClickListener {
            // Implement checkout functionality
            Toast.makeText(requireContext(), "Proceeding to checkout...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onIncrease = { viewModel.increaseQuantity(it.productId) },
            onDecrease = { viewModel.decreaseQuantity(it.productId) },
            onRemove = { viewModel.removeFromCart(it.productId) }
        )

        binding.recyclerCartitems.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun observeCartData() {
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.submitList(items)
            updateEmptyState(items.isEmpty())
        }

        viewModel.totalPrice.observe(viewLifecycleOwner) { price ->
            binding.tvTotalPrice.text = "$${String.format("%.2f", price ?: 0.0)}"
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.btnCheckout.isEnabled = false
            binding.btnCheckout.text = "Cart is Empty"
        } else {
            binding.btnCheckout.isEnabled = true
            binding.btnCheckout.text = "Go to Checkout"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}