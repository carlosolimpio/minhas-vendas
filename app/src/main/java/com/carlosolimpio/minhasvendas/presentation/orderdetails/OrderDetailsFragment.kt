package com.carlosolimpio.minhasvendas.presentation.orderdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.carlosolimpio.minhasvendas.R
import com.carlosolimpio.minhasvendas.databinding.FragmentOrderDetailsBinding
import com.carlosolimpio.minhasvendas.databinding.LayoutItemDetailsBinding
import com.carlosolimpio.minhasvendas.domain.core.UiState
import com.carlosolimpio.minhasvendas.domain.order.Item
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

    private val itemList by lazy { mutableListOf<Item>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchOrder()
        initObservers()
        initViews()
    }

    private fun fetchOrder() {
        if (isCreateNewOrder()) {
            viewModel.fetchOrderId() // create new order
        } else {
            viewModel.fetchOrderById(navArgs.orderId) // edit order
        }
    }

    private fun initObservers() {
        viewModel.orderIdState.observe(viewLifecycleOwner) { orderIdState ->
            when (orderIdState) {
                is UiState.Success -> {
                    initNewOrderViews(orderIdState.data)
                    onUserActionCancel(orderIdState.data)

                    binding.progressBar.visibility = View.GONE
                    binding.cardProgressBar.visibility = View.GONE
                }
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.cardProgressBar.visibility = View.VISIBLE

                }
                else -> {}
            }
        }

        viewModel.orderState.observe(viewLifecycleOwner) { orderState ->
            when (orderState) {
                is UiState.Success -> {
                    initOrderDetailsViews(orderState.data)
                    onUserActionCancel(orderState.data.number)

                    binding.progressBar.visibility = View.GONE
                    binding.cardProgressBar.visibility = View.GONE
                }
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.cardProgressBar.visibility = View.VISIBLE
                }
                else -> {}
            }
        }

        viewModel.toastState.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }
    }

    private fun initViews() {
        initOnSaveButton()
        initOnAddItemButton()
    }

    private fun initOnAddItemButton() {
        binding.apply {
            buttonAddItem.setOnClickListener {
                val dialog = AddItemDialog(null) { item ->
                    itemList.add(item)
                    textItemCountValue.text = itemList.count().toString()
                    textCardOrderTotalValue.text = requireContext().getString(
                        R.string.reais_value,
                        itemList.computeTotalOrderValue().toBRLCurrencyString()
                    )

                    addCardItemDescription(item)
                    shouldShowCardNoItemRegistered(false)
                }
                dialog.show(childFragmentManager, AddItemDialog.TAG)
            }
        }
    }

    private fun initOnSaveButton() {
        binding.apply {
            if (!isCreateNewOrder()) buttonSave.text = getString(R.string.edit_order)

            buttonSave.setOnClickListener {
                val order = Order(
                    number = textCardOrderNumberValue.text.toString().toLong(),
                    clientName = editTextClientName.text.toString(),
                    items = itemList
                )

                viewModel.saveOrder(order)
            }
        }
    }

    private fun initNewOrderViews(orderId: Long) {
        binding.apply {
            textCardOrderNumberValue.text = orderId.toString()
            textItemCountValue.text = getString(R.string.number_zero)
            textCardOrderTotalValue.text = getString(R.string.reais_value, getString(R.string.number_zero))
            shouldShowCardNoItemRegistered(true)
        }
    }

    private fun initOrderDetailsViews(order: Order) {
        binding.apply {
            textCardOrderNumberValue.text = order.number.toString()
            editTextClientName.setText(order.clientName)
            textItemCountValue.text = order.items.count().toString()
            textCardOrderTotalValue.text = requireContext().getString(
                R.string.reais_value,
                order.items.computeTotalOrderValue().toBRLCurrencyString()
            )
            shouldShowCardNoItemRegistered(order.items.isEmpty())
            order.items.forEach {
                itemList.add(it)
                addCardItemDescription(it)
            }
        }
    }

    private fun addCardItemDescription(item: Item) {
        val itemDetailsBinding = LayoutItemDetailsBinding.inflate(layoutInflater)
        itemDetailsBinding.apply {
            textItemName.text = item.name
            textQtdeValue.text = item.count.toString()
            textUnitaryValue.text = requireContext().getString(
                R.string.reais_value,
                item.value.toBRLCurrencyString()
            )
            textTotalItemValue.text = requireContext().getString(
                R.string.reais_value,
                item.computeTotalValue().toBRLCurrencyString()
            )

            iconEdit.setOnClickListener {
                val dialog = AddItemDialog(item) { newItem ->
                    itemList.remove(item)
                    binding.layoutItems.removeView(itemDetailsBinding.root)

                    itemList.add(newItem)

                    textItemName.text = newItem.name
                    textQtdeValue.text = newItem.count.toString()
                    textUnitaryValue.text = requireContext().getString(
                        R.string.reais_value,
                        newItem.value.toBRLCurrencyString()
                    )
                    textTotalItemValue.text = requireContext().getString(
                        R.string.reais_value,
                        newItem.computeTotalValue().toBRLCurrencyString()
                    )
                    binding.layoutItems.addView(itemDetailsBinding.root)
                }
                dialog.show(childFragmentManager, AddItemDialog.TAG)
            }

            iconRemove.setOnClickListener {
                itemList.remove(item)
                binding.layoutItems.removeView(itemDetailsBinding.root)
            }
        }

        binding.layoutItems.addView(itemDetailsBinding.root)
    }

    /**
     * Used for back button press and cancel button press
     */
    private fun onUserActionCancel(orderId: Long) {
        binding.buttonCancel.setOnClickListener {
            removeOrder(orderId)
            findNavController().popBackStack()
        }

        onBackPressedCustomAction { removeOrder(orderId) }
    }

    private fun removeOrder(orderId: Long) {
        if (isCreateNewOrder()) viewModel.deleteOrder(orderId)
    }

    private fun isCreateNewOrder() = navArgs.orderId == CREATE_ORDER_ID

    private fun shouldShowCardNoItemRegistered(isVisible: Boolean) {
        binding.cardNoItemRegistered.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
