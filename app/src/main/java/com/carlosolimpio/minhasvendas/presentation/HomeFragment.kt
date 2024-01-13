package com.carlosolimpio.minhasvendas.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carlosolimpio.minhasvendas.databinding.FragmentHomeBinding
import com.carlosolimpio.minhasvendas.domain.core.UiState
import com.carlosolimpio.minhasvendas.domain.order.Item
import com.carlosolimpio.minhasvendas.domain.order.Order
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {
    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel: OrderViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchOrderId()
//        viewModel.fetchAllOrders()
        initObservers()
    }

    private fun initObservers() {
        viewModel.orderListState.observe(viewLifecycleOwner) { orderListState ->
            when (orderListState) {
                is UiState.Success -> {
                    Log.d("olimpio", "success: ${orderListState.data}")
                }
                is UiState.NotFound -> {
                    Log.d("olimpio", "not found: ${orderListState.message}")
                }
                is UiState.Error -> {
                    Log.d("olimpio", "error: ${orderListState.error}")
                }
                UiState.Loading -> {
                    Log.d("olimpio", "loading")
                }
            }
        }

        viewModel.orderIdState.observe(viewLifecycleOwner) { orderIdState ->
            when (orderIdState) {
                is UiState.Success -> {
                    Log.d("olimpio", "orderId = ${orderIdState.data}")
                    viewModel.saveOrder(
                        Order(
                            number = orderIdState.data,
                            clientName = "Carlos",
                            items = listOf(
                                Item("Mouse", 100, 10.0)
                            )
                        )
                    )

                    viewModel.fetchAllOrders()
                }
                is UiState.Loading -> {
                    // loading na view
                    Log.d("olimpio", "loading orderid")

                }
                else -> {
                    /* handle possible error */
                }
            }
        }
    }
}
