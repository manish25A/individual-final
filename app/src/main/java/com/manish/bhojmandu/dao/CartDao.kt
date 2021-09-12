package com.manish.bhojmandu.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.manish.bhojmandu.entity.CartItem

@Dao
interface CartDao {
    @Query("Delete from cartItem")
    suspend fun deleteCartItem()
    @Insert
    suspend fun insertCart(itemCartModel:List<CartItem>)
}