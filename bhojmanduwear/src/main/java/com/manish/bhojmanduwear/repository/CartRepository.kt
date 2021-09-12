package com.manish.bhojmandu.repository

import com.manish.bhojmandu.api.APIRequest
import com.manish.bhojmandu.api.ServiceBuilder
import com.manish.bhojmandu.response.cartResponse.CartInsertResponse
import com.manish.bhojmandu.response.cartResponse.CartItemResponse
import com.manish.bhojmandu.response.cartResponse.DeleteCartResponse
import com.manish.bhojmanduwear.api.cartAPI


class CartRepository: APIRequest(){
    val cartAPI= ServiceBuilder.buildService((cartAPI::class.java))

    suspend fun  getCart(): CartItemResponse {
        return apiRequest {
            cartAPI.getCart(ServiceBuilder.token!!)
        }
    }
    suspend fun insertCart(id: String): CartInsertResponse {
        return  apiRequest {
            cartAPI.insertCart(ServiceBuilder.token!!,id)
        }
    }
    suspend fun deleteCart(id: String): DeleteCartResponse {
        return apiRequest {
            cartAPI.deleteCart(ServiceBuilder.token!!,id)
        }
    }

}