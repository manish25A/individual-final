package com.manish.bhojmandu.repository

import com.manish.bhojmandu.api.CustomerDataAPI
import com.manish.bhojmandu.api.APIRequest
import com.manish.bhojmandu.api.ServiceBuilder
import com.manish.bhojmandu.entity.Customer
import com.manish.bhojmandu.response.customerResponse.CustomerLoginResponse
import com.manish.bhojmandu.response.customerResponse.CustomerRegisterResponse
import com.manish.bhojmandu.response.customerResponse.CustomerResponse
import com.manish.bhojmandu.response.customerResponse.CustomerUpdateResponse

class CustomerRepository:APIRequest(){
        val api=ServiceBuilder.buildService((CustomerDataAPI::class.java))

        suspend fun registerCustomer(customer:Customer): CustomerRegisterResponse {
            return apiRequest {
                api.SignupCustomer(customer)
            }
        }

        suspend fun validateCustomer(email:String,password:String): CustomerLoginResponse {
            return apiRequest {
                api.validateCustomer(email,password)
            }
        }
    suspend fun  getCustomer(): CustomerResponse {
        return apiRequest {
            api.getCustomer(ServiceBuilder.token!!)
        }
    }

    suspend fun updateCustomer(customer: Customer): CustomerUpdateResponse {
        return apiRequest {
            api.updateCustomer(ServiceBuilder.token!!,customer)
        }
    }
    suspend fun updatePassword(customer: Customer): CustomerUpdateResponse {
        return apiRequest {
            api.updateCustomer(ServiceBuilder.token!!,customer)
        }
    }

    }