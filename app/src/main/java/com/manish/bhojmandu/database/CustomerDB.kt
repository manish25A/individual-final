package com.manish.bhojmandu.database

import android.content.Context
import androidx.room.*
import com.manish.bhojmandu.dao.CartDao
import com.manish.bhojmandu.dao.CustomerDao
import com.manish.bhojmandu.dao.ProductsDao
import com.manish.bhojmandu.dao.VendorDao
import com.manish.bhojmandu.database.typeConverters.CustomerConverter
import com.manish.bhojmandu.database.typeConverters.ItemConverter
import com.manish.bhojmandu.entity.Customer
import com.manish.bhojmandu.entity.ProductsModel
import com.manish.bhojmandu.entity.Vendors
import com.manish.bhojmandu.entity.CartItem

@Database(
    entities = [(Customer::class), (ProductsModel::class),(Vendors::class),(CartItem::class)], version = 1
)
@TypeConverters(
    ItemConverter::class,
    CustomerConverter::class
)
abstract class CustomerDB : RoomDatabase() {
    abstract fun getCustomerDao(): CustomerDao

    abstract fun ProductsDao(): ProductsDao

    abstract fun VendorDao():VendorDao

    abstract fun CartDao():CartDao

    companion object {
        @Volatile
        private var instance: CustomerDB? = null
        fun getInstance(context: Context): CustomerDB {
            if (instance == null) {
                synchronized(CustomerDB::class) {
                    instance = builderDatabase(context)
                }
            }
            return instance!!
        }

        private fun builderDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CustomerDB::class.java,
                "CustomerDatabase"
            ).build()
    }
}