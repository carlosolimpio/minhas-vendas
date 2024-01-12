package com.carlosolimpio.minhasvendas.data.local

import com.carlosolimpio.minhasvendas.data.local.entity.ItemEntity
import com.carlosolimpio.minhasvendas.data.local.entity.OrderEntity
import com.carlosolimpio.minhasvendas.domain.order.Item
import com.carlosolimpio.minhasvendas.domain.order.Order

fun Order.toOrderEntity() = OrderEntity(
    id = number,
    clientName = clientName,
    items = items.map { it.toItemEntity() }
)

fun Item.toItemEntity() = ItemEntity(
    name = name,
    count = count,
    value = value
)

fun OrderEntity.toOrder() = Order(
    number = id,
    clientName = clientName,
    items = items.map { it.toItem() }
)

fun ItemEntity.toItem() = Item(
    name = name,
    count = count,
    value = value
)
