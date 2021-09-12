package com.manish.bhojmandu.api

import com.manish.bhojmandu.response.cartResponse.CartInsertResponse
import com.manish.bhojmandu.response.cartResponse.CartItemResponse
import com.manish.bhojmandu.response.cartResponse.DeleteCartResponse
import retrofit2.Response
import retrofit2.http.*

interface cartAPI {
    @POST("/cart/{id}")
    suspend fun insertCart(
        @Header("Authorization") token:String,
        @Path("id" ) id:String
    ): Response<CartInsertResponse>

    @GET("cart/all")
    suspend fun getCart(
        @Header("Authorization") token: String,
    ): Response<CartItemResponse>

    @DELETE("cart/{id}")
    suspend fun deleteCart(
        @Header("Authorization") token:String,
        @Path("id") id:String
    ): Response<DeleteCartResponse>

    @DELETE("cart/checkout")
    suspend fun checkoutDelete(
        @Header("Authorization") token:String,
        @Path("id") id:String
    ): Response<DeleteCartResponse>
}