package com.manish.bhojmandu.api

import com.manish.bhojmandu.response.productResponse.VendorItemResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface productsAPI {

    @GET("product/getProducts/{vendorName}")
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Path("vendorName") vendorName:String
    ): Response<VendorItemResponse>
}