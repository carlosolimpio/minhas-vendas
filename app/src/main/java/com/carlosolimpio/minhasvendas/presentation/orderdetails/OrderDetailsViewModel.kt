package com.carlosolimpio.minhasvendas.presentation.orderdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosolimpio.minhasvendas.domain.core.OrderNotFoundException
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

    private val _isDeletedState = MutableLiveData<UiState<Boolean>>(UiState.Loading)
    val isDeletedState: LiveData<UiState<Boolean>>
        get() = _isDeletedState

    fun fetchOrderId() {
        viewModelScope.launch(Dispatchers.IO) {
            val orderId = repository.retrieveOrderId()

            orderId.data?.let { _orderIdState.postValue(UiState.Success(it)) }
            orderId.error?.let { _orderIdState.postValue(UiState.Error(it)) }
        }
    }

    fun deleteOrder(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val isDeleted = repository.deleteOrderId(id)

            isDeleted.data?.let { _isDeletedState.postValue(UiState.Success(it)) }
            isDeleted.error?.let { _isDeletedState.postValue(UiState.Error(it)) }
        }
    }

    // falta validar os dados
    fun saveOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveOrder(order)
        }
    }

    fun fetchOrderById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val order = repository.getOrderFromId(id)

            order.data?.let { _orderState.postValue(UiState.Success(it)) }
            order.error?.let {
                when (it) {
                    is OrderNotFoundException -> {
                        _orderState.postValue(UiState.NotFound(it.message!!))
                    }
                    else -> {
                        _orderState.postValue(UiState.Error(it))
                    }
                }
            }
        }
    }
}