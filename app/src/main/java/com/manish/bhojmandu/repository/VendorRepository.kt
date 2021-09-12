package com.manish.bhojmandu.repository

import com.manish.bhojmandu.api.APIRequest
import com.manish.bhojmandu.api.CustomerDataAPI
import com.manish.bhojmandu.api.ServiceBuilder
import com.manish.bhojmandu.api.getVendorsAPI
import com.manish.bhojmandu.entity.Customer
import com.manish.bhojmandu.entity.VendorModel
import com.manish.bhojmandu.response.customerResponse.CustomerLoginResponse
import com.manish.bhojmandu.response.customerResponse.CustomerRegisterResponse
import com.manish.bhojmandu.response.customerResponse.CustomerResponse
import com.manish.bhojmandu.response.customerResponse.CustomerUpdateResponse
import com.manish.bhojmandu.response.productResponse.VendorItemResponse
import com.manish.bhojmandu.response.productResponse.VendorResponse

class VendorRepository: APIRequest(){
    val api= ServiceBuilder.buildService((getVendorsAPI::class.java))

    suspend fun  getVendor(vendorName:String): VendorResponse {
        return  apiRequest {
            api.getVendor(ServiceBuilder.token!!,vendorName)
        }
    }

}