package com.carlosolimpio.minhasvendas.presentation

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

class HomeViewModel(private val repository: OrderRepository) : ViewModel() {
    private val _orderListState = MutableLiveData<UiState<List<Order>>>(UiState.Loading)
    val orderListState: LiveData<UiState<List<Order>>>
        get() = _orderListState

    fun fetchAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            val orders = repository.getAllOrders()

            orders.data?.let { _orderListState.postValue(UiState.Success(it)) }
            orders.error?.let {
                when (it) {
                    is OrderNotFoundException -> {
                        _orderListState.postValue(UiState.NotFound(it.message!!))
                    }
                    else -> {
                        _orderListState.postValue(UiState.Error(it))
                    }
                }
            }
        }
    }
}
