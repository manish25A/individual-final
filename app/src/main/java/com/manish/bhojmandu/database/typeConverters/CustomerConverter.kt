package com.manish.bhojmandu.database.typeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.manish.bhojmandu.entity.Customer

class CustomerConverter {
    @TypeConverter
    fun listToJson(value:List<Customer>?) = Gson().toJson(value)
    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value,Array<Customer>::class.java).toList()
}