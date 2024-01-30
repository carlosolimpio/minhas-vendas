package com.carlosolimpio.minhasvendas.domain.order

sealed class ListResult {
    data class Header(val date: String): ListResult()
    data class OrderList(val order: Order): ListResult()
}
