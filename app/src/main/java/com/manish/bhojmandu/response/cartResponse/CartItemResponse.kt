package com.manish.bhojmandu.response.cartResponse

import com.manish.bhojmandu.entity.CartItem

class CartItemResponse (
    val success: Boolean? = null,
    val  data: MutableList<CartItem>? = null,
    val token : String?=null
)