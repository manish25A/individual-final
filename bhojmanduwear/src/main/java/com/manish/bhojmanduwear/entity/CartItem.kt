package com.manish.bhojmandu.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartItem (
    @PrimaryKey(autoGenerate = false)
    val _id: String = "",
    val itemId :List<ProductsModel>?=null,
    val customerId :List<Customer>?=null,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
    parcel.createTypedArrayList(ProductsModel),
        parcel.createTypedArrayList(Customer)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeTypedList(itemId)
        parcel.writeTypedList(customerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}