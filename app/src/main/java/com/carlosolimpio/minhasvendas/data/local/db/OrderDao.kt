package com.carlosolimpio.minhasvendas.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carlosolimpio.minhasvendas.data.local.entity.OrderEntity

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Query("SELECT * FROM order_table WHERE id = :id")
    suspend fun getOrderFromId(id: Long): OrderEntity?

    @Query("SELECT * FROM order_table")
    suspend fun getAllOrders(): List<OrderEntity>

    @Query("DELETE FROM order_table WHERE id = :id")
    suspend fun deleteOrder(id: Long): Int
}
