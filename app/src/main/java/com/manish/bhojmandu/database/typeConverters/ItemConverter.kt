package com.manish.bhojmandu.database.typeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.manish.bhojmandu.entity.ProductsModel

class ItemConverter { @TypeConverter
fun listToJson(value:List<ProductsModel>?) = Gson().toJson(value)
    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value,Array<ProductsModel>::class.java).toList()
}