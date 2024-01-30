package com.carlosolimpio.minhasvendas.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.carlosolimpio.minhasvendas.R
import com.carlosolimpio.minhasvendas.databinding.LayoutDateOrdersBinding
import com.carlosolimpio.minhasvendas.databinding.LayoutItemOrderBinding
import com.carlosolimpio.minhasvendas.domain.order.ListResult
import com.carlosolimpio.minhasvendas.domain.order.Order
import com.carlosolimpio.minhasvendas.domain.order.computeTotalOrderValue
import com.carlosolimpio.minhasvendas.presentation.extensions.toBRLCurrencyString

class OrderListAdapter(
    private val onOrderClick: (orderId: Long) -> Unit
) : RecyclerView.Adapter<OrderListAdapter.ListViewHolder>() {

    private val list = mutableListOf<ListResult>()

    fun submitData(newList: List<ListResult>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val vh = when (viewType) {
            0 -> {
                DateViewHolder(LayoutDateOrdersBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ))
            }
            1 -> {
                OrderViewHolder(LayoutItemOrderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ))
            }
            else -> {
                throw Exception("layout not found")
            }
        }

        return vh
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        when (holder) {
            is DateViewHolder -> {
                holder.bind((list[position] as ListResult.Header).date)
            }
            is OrderViewHolder -> {
                holder.bind((list[position] as ListResult.OrderList).order)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is ListResult.Header) {
            0
        } else if (list[position] is ListResult.OrderList) {
            1
        } else {
            throw Exception("invalid viewtype")
        }
    }

    abstract inner class ListViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    inner class OrderViewHolder(
        private val binding: LayoutItemOrderBinding
    ) : ListViewHolder(binding) {

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

    inner class DateViewHolder(
        private val binding: LayoutDateOrdersBinding
    ) : ListViewHolder(binding) {
        fun bind(date: String) {
            binding.textDateValue.text = date
        }
    }
}
