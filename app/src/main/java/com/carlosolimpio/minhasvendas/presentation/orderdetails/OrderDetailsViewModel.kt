package com.carlosolimpio.minhasvendas.presentation.orderdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosolimpio.minhasvendas.domain.core.UiState
import com.carlosolimpio.minhasvendas.domain.order.Order
import com.carlosolimpio.minhasvendas.domain.order.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderDetailsViewModel(private val repository: OrderRepository) : ViewModel() {
    private val _orderIdState = MutableLiveData<UiState<Long>>(UiState.Loading)
    val orderIdState: LiveData<UiState<Long>>
        get() = _orderIdState

    private val _orderState = MutableLiveData<UiState<Order>>(UiState.Loading)
    val orderState: LiveData<UiState<Order>>
        get() = _orderState

    private val _toastState = MutableLiveData<String>()
    val toastState: LiveData<String>
        get() = _toastState

    fun fetchOrderId() {
        viewModelScope.launch(Dispatchers.IO) {
            val orderId = repository.retrieveOrderId()

            orderId.data?.let { _orderIdState.postValue(UiState.Success(it)) }
            orderId.error?.let { _toastState.postValue(it.message) }
        }
    }

    fun deleteOrder(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val isDeleted = repository.deleteOrderId(id)

            isDeleted.data?.let { _toastState.postValue("Order deleted with success!") }
            isDeleted.error?.let { _toastState.postValue(it.message) }
        }
    }

    fun saveOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            val isSaved = repository.saveOrder(order)

            isSaved.data?.let { _toastState.postValue("Order saved with success!") }
            isSaved.error?.let { _toastState.postValue(it.message) }
        }
    }

    fun fetchOrderById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val order = repository.getOrderFromId(id)

            order.data?.let { _orderState.postValue(UiState.Success(it)) }
            order.error?.let { _toastState.postValue(it.message) }
        }
    }
}