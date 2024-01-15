package com.carlosolimpio.minhasvendas.domain.order

import com.carlosolimpio.minhasvendas.domain.core.Resource

interface OrderRepository {
    suspend fun retrieveOrderId(): Resource<Long>
    suspend fun deleteOrderId(id: Long): Resource<Boolean>
    suspend fun saveOrder(order: Order)
    suspend fun getOrderFromId(id: Int): Resource<Order?>
    suspend fun getAllOrders(): Resource<List<Order>>
}
