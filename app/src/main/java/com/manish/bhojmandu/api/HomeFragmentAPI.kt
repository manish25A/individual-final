package com.manish.bhojmandu.api

import com.manish.bhojmandu.response.productResponse.ShowHomeItemsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface HomeFragmentAPI {

    @GET("vendor/auth/")
    suspend fun getAllProducts(
        @Header("Authorization") token:String,

    ):Response<ShowHomeItemsResponse>
}