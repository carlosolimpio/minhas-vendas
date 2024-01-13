package com.carlosolimpio.minhasvendas.domain.order

data class Order(
    val number: Long,
    val clientName: String,
    val items: List<Item>
)

data class Item(
    val name: String,
    val count: Int,
    val value: Double
)