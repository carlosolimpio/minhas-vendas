package com.carlosolimpio.minhasvendas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

@Entity(tableName = "order_table")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clientName: String,
    val items: List<ItemEntity>
)

class OrderEntityConverter {
    @TypeConverter
    fun fromList(value: List<ItemEntity>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<ItemEntity>>(value)
}
