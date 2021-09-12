package com.manish.bhojmandu.response.productResponse


import com.manish.bhojmandu.entity.VendorModel

data class VendorResponse(
    val success: Boolean? = null,
    val data: MutableList<VendorModel>? = null
)
