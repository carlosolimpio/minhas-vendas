package com.carlosolimpio.minhasvendas.data.local.entity

import kotlinx.serialization.Serializable

@Serializable
data class ItemEntity(
    val name: String,
    val count: Int,
    val value: Double
)
