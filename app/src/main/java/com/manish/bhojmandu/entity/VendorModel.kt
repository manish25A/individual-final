package com.manish.bhojmandu.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VendorModel(
    @PrimaryKey(autoGenerate = false)
    val _id: String = "",
    val vendorName: String? = null,
    val vendorEmail: String? = null,
    val photo: String? = null,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(vendorName)
        parcel.writeString(vendorEmail)
        parcel.writeString(photo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VendorModel> {
        override fun createFromParcel(parcel: Parcel): VendorModel {
            return VendorModel(parcel)
        }

        override fun newArray(size: Int): Array<VendorModel?> {
            return arrayOfNulls(size)
        }
    }
}