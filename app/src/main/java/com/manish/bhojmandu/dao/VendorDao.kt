package com.manish.bhojmandu.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import com.manish.bhojmandu.entity.Vendors

@Dao
interface VendorDao {
    @Query("Delete from Vendors")
    suspend fun deleteVendor()
    @Insert
    suspend fun  insertVendor(Vendors: MutableList<Vendors>?)
}