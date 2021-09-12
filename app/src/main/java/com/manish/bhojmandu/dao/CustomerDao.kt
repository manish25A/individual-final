package com.manish.bhojmandu.dao

import androidx.room.*
import com.manish.bhojmandu.entity.Customer

@Dao
interface CustomerDao {
    @Insert
    suspend fun registerCustomer(customer: Customer)

    @Query("Select * From Customer where  email=(:email) and password=(:password)")
    suspend fun checkCustomer(email:String, password:String): Customer

    @Update
    suspend fun updateCustomerInfo(customer: Customer)

    @Query("Select * from Customer")
    suspend fun getCustomerData():List<Customer>
    @Delete
    suspend fun  deleteCustomer(customer: Customer)
}