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
    suspend fun getOrderFromId(id: Int): OrderEntity?

    @Query("SELECT * FROM order_table")
    suspend fun getAllOrders(): List<OrderEntity>

/*    @Update()
    suspend fun updateOrder()

    @Delete
    suspend fun deleteOrder()*/
}
