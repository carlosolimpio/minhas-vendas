package com.carlosolimpio.minhasvendas.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.carlosolimpio.minhasvendas.R
import com.carlosolimpio.minhasvendas.databinding.LayoutItemOrderBinding
import com.carlosolimpio.minhasvendas.domain.order.Order
import com.carlosolimpio.minhasvendas.domain.order.computeTotalOrderValue
import com.carlosolimpio.minhasvendas.presentation.extensions.toBRLCurrencyString

class OrderListAdapter(
    private val onOrderClick: (orderId: Long) -> Unit
) : RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.number == newItem.number
        }
        override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun submitData(list: List<Order>) {
        asyncListDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = LayoutItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return OrderViewHolder(binding)
    }

    override fun getItemCount() = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

    inner class OrderViewHolder(
        private val binding: LayoutItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.apply {
                textOrderNumberValue.text = order.number.toString()
                textClientValue.text = order.clientName
                textOrderTotalValue.text = root.context.getString(
                    R.string.reais_value,
                    order.items.computeTotalOrderValue().toBRLCurrencyString()
                )

                cardItemOrder.setOnClickListener { onOrderClick(order.number) }
            }
        }
    }
}
