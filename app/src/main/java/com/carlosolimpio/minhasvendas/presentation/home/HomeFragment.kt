package com.carlosolimpio.minhasvendas.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosolimpio.minhasvendas.R
import com.carlosolimpio.minhasvendas.databinding.FragmentHomeBinding
import com.carlosolimpio.minhasvendas.domain.core.UiState
import com.carlosolimpio.minhasvendas.domain.order.Order
import com.carlosolimpio.minhasvendas.domain.order.computeTotalSalesValue
import com.carlosolimpio.minhasvendas.presentation.extensions.toBRLCurrencyString
import org.koin.android.ext.android.inject

const val CREATE_ORDER_ID = -1L

class HomeFragment : Fragment() {
    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by inject()

    private val orderListAdapter by lazy {
        OrderListAdapter { order -> showOrderDetails(order) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeOrderList()
        initFab()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchAllOrders()
    }

    private fun observeOrderList() {
        viewModel.orderListState.observe(viewLifecycleOwner) { orderListState ->
            when (orderListState) {
                is UiState.Success -> {
                    initOrderList(orderListState.data)
                    initTotalSalesView(orderListState.data)

                    binding.pbOrderList.visibility = View.GONE
                    binding.cardOrdersNotFound.visibility = View.GONE
                }
                is UiState.NotFound -> {
                    binding.cardOrdersNotFound.visibility = View.VISIBLE
                    binding.pbOrderList.visibility = View.GONE
                }
                is UiState.Error -> {
                    binding.pbOrderList.visibility = View.GONE
                    Toast.makeText(context, orderListState.error.message, Toast.LENGTH_LONG).show()
                }
                UiState.Loading -> {
                    binding.pbOrderList.visibility = View.VISIBLE
                    binding.pbTotalSalesValue.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initTotalSalesView(orderList: List<Order>) {
        binding.apply {
            pbTotalSalesValue.visibility = View.GONE
            textTotalSalesValue.visibility = View.VISIBLE
            textTotalSalesValue.text = requireContext().getString(
                R.string.reais_value,
                orderList.computeTotalSalesValue().toBRLCurrencyString()
            )
        }
    }

    private fun initOrderList(orderList: List<Order>) {
        orderListAdapter.submitData(orderList)

        with(binding.rvOrders) {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showOrderDetails(orderId: Long) {
        navigateToOrderDetailsScreenWithId(orderId)
    }

    private fun initFab() {
        binding.fabCreateOrder.setOnClickListener {
            navigateToOrderDetailsScreenWithId()
        }
    }

    private fun navigateToOrderDetailsScreenWithId(id: Long = CREATE_ORDER_ID) {
        val action = HomeFragmentDirections.actionHomeFragmentToOrderDetailsFragment(id)
        findNavController().navigate(action)
    }
}
