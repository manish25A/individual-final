package com.manish.bhojmandu.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manish.bhojmandu.entity.ProductsModel

@Dao
interface ProductsDao {
    @Query("SELECT * FROM ProductsModel")
    suspend fun getAllProducts() : MutableList<ProductsModel>

    @Query("Delete from ProductsModel")
    suspend fun deleteProduct()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertProduct(ProductsModel: MutableList<ProductsModel>?)
}