package com.manish.bhojmandu.response.productResponse


import com.manish.bhojmandu.entity.Vendors

data class ShowHomeItemsResponse (
    val success: Boolean? = null,
    val data: MutableList<Vendors>? = null
)