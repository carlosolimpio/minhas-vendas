package com.carlosolimpio.minhasvendas.domain.order

data class Order(
    val number: Long,
    val clientName: String,
    val date: String,
    val items: List<Item>
)

data class Item(
    val name: String,
    val count: Int,
    val value: Double
)

fun List<Order>.computeTotalSalesValue() = this.sumOf { it.items.computeTotalOrderValue() }
fun List<Item>.computeTotalOrderValue() = this.sumOf { it.count * it.value }
fun Item.computeTotalValue() = this.count * this.value
