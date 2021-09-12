package com.manish.bhojmandu.repository

import android.content.Context
import com.manish.bhojmandu.api.APIRequest
import com.manish.bhojmandu.api.ServiceBuilder
import com.manish.bhojmandu.api.productsAPI
import com.manish.bhojmandu.database.CustomerDB
import com.manish.bhojmandu.entity.Customer
import com.manish.bhojmandu.entity.ProductsModel
import com.manish.bhojmandu.response.productResponse.VendorItemResponse

class ProductsRepository : APIRequest() {

    private val productsAPI = ServiceBuilder.buildService(productsAPI::class.java)

    suspend fun  getProducts(vendorName:String): VendorItemResponse{
        return  apiRequest {
            productsAPI.getProducts(ServiceBuilder.token!!,vendorName)
        }
    }

}