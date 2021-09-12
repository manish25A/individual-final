package com.manish.bhojmandu.api

import com.manish.bhojmandu.entity.Customer
import com.manish.bhojmandu.response.customerResponse.CustomerLoginResponse
import com.manish.bhojmandu.response.customerResponse.CustomerRegisterResponse
import com.manish.bhojmandu.response.customerResponse.CustomerResponse
import com.manish.bhojmandu.response.customerResponse.CustomerUpdateResponse

import retrofit2.Response
import retrofit2.http.*

interface CustomerDataAPI {


    ///Add Client / user// buyer
    @POST("/customer/auth/register")
    suspend fun SignupCustomer(
        @Body customer: Customer
    ): Response<CustomerRegisterResponse>

    //Login user
    @FormUrlEncoded
    @POST("customer/auth/login")
    suspend fun validateCustomer(
        @Field("email") email :String,
        @Field("password") password : String
    ) : Response<CustomerLoginResponse>

    @GET("customer/auth/me")
    suspend fun getCustomer(
        @Header("Authorization") token: String,
    ):Response<CustomerResponse>

    @PUT("customer/auth/update")
    suspend fun updateCustomer(
        @Header("Authorization") token: String,
        @Body customer: Customer
    ): Response<CustomerUpdateResponse>
    
    @PUT("customer/auth/update")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body customer: Customer
    ): Response<CustomerUpdateResponse>
}