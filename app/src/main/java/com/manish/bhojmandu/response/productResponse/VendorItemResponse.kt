package com.manish.bhojmandu.response.productResponse

import com.manish.bhojmandu.entity.ProductsModel


data class VendorItemResponse(
    val success: Boolean? = null,
    val data: MutableList<ProductsModel>? = null
)
