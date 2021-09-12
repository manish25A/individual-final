package com.manish.bhojmandu.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Vendors(
    @PrimaryKey(autoGenerate = false)
    val _id: String = "",
    val vendorName: String? = null,
    val vendorEmail: String? = null,
    val vendorAddress: String? = null,
    val photo: String? = null,
)