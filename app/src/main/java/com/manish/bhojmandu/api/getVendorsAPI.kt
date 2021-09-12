package com.manish.bhojmandu.api

import com.manish.bhojmandu.response.productResponse.ShowHomeItemsResponse
import com.manish.bhojmandu.response.productResponse.VendorResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface getVendorsAPI {
    @GET("vendor/auth/")
    suspend fun getVendorData(
        @Header("Authorization") token: String
    ): Response<ShowHomeItemsResponse>


    @GET("vendor/auth/{vendorName}")
    suspend fun getVendor(
        @Header("Authorization") token: String,
        @Path("vendorName") vendorName: String
    ): Response<VendorResponse>


}