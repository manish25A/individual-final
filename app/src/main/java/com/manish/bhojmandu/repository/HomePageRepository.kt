package com.manish.bhojmandu.repository

import com.manish.bhojmandu.api.APIRequest

import com.manish.bhojmandu.api.ServiceBuilder
import com.manish.bhojmandu.response.productResponse.ShowHomeItemsResponse
import com.manish.bhojmandu.api.HomeFragmentAPI
import com.manish.bhojmandu.api.getVendorsAPI

class HomePageRepository : APIRequest() {
    val vendorsAPI = ServiceBuilder.buildService(getVendorsAPI::class.java)

    suspend fun getVendors(): ShowHomeItemsResponse {
        return apiRequest {
            vendorsAPI.getVendorData(ServiceBuilder.token!!)
        }
    }
}