package com.carlosolimpio.minhasvendas.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.carlosolimpio.minhasvendas.data.local.entity.OrderEntity
import com.carlosolimpio.minhasvendas.data.local.entity.OrderEntityConverter

@Database(entities = [OrderEntity::class], version = 1, exportSchema = false)
@TypeConverters(OrderEntityConverter::class)
abstract class OrderRoomDatabase : RoomDatabase() {
    abstract fun getOrdersDao(): OrderDao
}
