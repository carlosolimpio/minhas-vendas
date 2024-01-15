package com.carlosolimpio.minhasvendas.presentation.orderdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.carlosolimpio.minhasvendas.R
import com.carlosolimpio.minhasvendas.databinding.FragmentOrderDetailsBinding
import com.carlosolimpio.minhasvendas.databinding.LayoutItemDetailsBinding
import com.carlosolimpio.minhasvendas.domain.core.UiState
import com.carlosolimpio.minhasvendas.domain.order.Order
import com.carlosolimpio.minhasvendas.domain.order.computeTotalOrderValue
import com.carlosolimpio.minhasvendas.domain.order.computeTotalValue
import com.carlosolimpio.minhasvendas.presentation.extensions.onBackPressedCustomAction
import com.carlosolimpio.minhasvendas.presentation.extensions.toBRLCurrencyString
import com.carlosolimpio.minhasvendas.presentation.home.CREATE_ORDER_ID
import org.koin.android.ext.android.inject

class OrderDetailsFragment : Fragment() {

    private val binding by lazy { FragmentOrderDetailsBinding.inflate(layoutInflater) }
    private val navArgs: OrderDetailsFragmentArgs by navArgs()
    private val viewModel: OrderDetailsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (navArgs.orderId == CREATE_ORDER_ID) {
            // create new order
            viewModel.fetchOrderId()
        } else {
            // edit order
            viewModel.fetchOrderById(navArgs.orderId)
        }

        initObservers()
    }

    private fun initObservers() {
        viewModel.orderIdState.observe(viewLifecycleOwner) { orderIdState ->
            when (orderIdState) {
                is UiState.Success -> {
                    binding.textCardOrderNumberValue.text = orderIdState.data.toString()
                    onBackPressedCustomAction { viewModel.deleteOrder(orderIdState.data) }
                }
                is UiState.Loading -> {}
                else -> { /*handle possible error*/ }
            }
        }

        viewModel.orderState.observe(viewLifecycleOwner) { orderState ->
            when (orderState) {
                is UiState.Success -> {
                    initOrderDetailsScreen(orderState.data)
                }
                is UiState.NotFound -> {}
                is UiState.Error -> {
                    Toast.makeText(context, orderState.error.message, Toast.LENGTH_LONG).show()
                }
                UiState.Loading -> {}
            }
        }
    }

    private fun initOrderDetailsScreen(order: Order) {
        binding.apply {
            textCardOrderNumberValue.text = order.number.toString()
            editTextClientName.setText(order.clientName)
            textItemCountValue.text = order.items.count().toString()
            textCardOrderTotalValue.text = requireContext().getString(
                R.string.reais_value,
                order.items.computeTotalOrderValue().toBRLCurrencyString()
            )
            cardNoItemRegistered.visibility = if (order.items.isEmpty()) View.VISIBLE else View.GONE
            order.items.forEach {
                val itemDetailsBinding = LayoutItemDetailsBinding.inflate(layoutInflater)
                itemDetailsBinding.apply {
                    textItemName.text = it.name
                    textQtdeValue.text = it.count.toString()
                    textUnitaryValue.text = requireContext().getString(
                        R.string.reais_value,
                        it.value.toBRLCurrencyString()
                    )
                    textTotalItemValue.text = requireContext().getString(
                        R.string.reais_value,
                        it.computeTotalValue().toBRLCurrencyString()
                    )
                }
                layoutItems.addView(itemDetailsBinding.root)
            }
        }
    }


}
