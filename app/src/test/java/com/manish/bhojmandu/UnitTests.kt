package com.manish.bhojmandu

import com.manish.bhojmandu.api.ServiceBuilder
import com.manish.bhojmandu.entity.Customer
import com.manish.bhojmandu.repository.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UnitTests {


    private lateinit var customerRepository: CustomerRepository
    private lateinit var cartRepository: CustomerRepository
    private lateinit var showHomePageRepository: HomePageRepository

    // -----------------------------User Testing-----------------------------
    @Test
    fun checkBuyer() = runBlocking {
        customerRepository = CustomerRepository()
        val response = customerRepository.validateCustomer("manish@gmail.com", "manish12")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun registerCustomer() = runBlocking {
        val customer =
            Customer(
                fname = "Test User",
                lname = "Test User Last Name",
                email = "test@gmail.com",
                number = "1234567890",
                password = "apple"
            )
        customerRepository = CustomerRepository()
        val response = customerRepository.registerCustomer(customer)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }


    //     -----------------------------Adding Item To Cart Testing-----------------------------
    @Test
    fun addToCart() = runBlocking {
        customerRepository = CustomerRepository()

        ServiceBuilder.token =
            "Bearer " + customerRepository.validateCustomer("manish@gmail.com", "manish12").token
        val repository = CartRepository()
        val response = repository.insertCart(id = "60705bd02677d31114b79a7f")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    //     -----------------------------User Info Update Testing-----------------------------
    @Test
    fun updateCustomer() = runBlocking {
        customerRepository = CustomerRepository()
        val customer =
            Customer(number = "1234567890")
        ServiceBuilder.token =
            "Bearer " + customerRepository.validateCustomer("manish@gmail.com", "manish12").token
        val expectedResult = true
        val actualResult = customerRepository.updateCustomer(customer).success
        Assert.assertEquals(expectedResult, actualResult)

    }
//     -----------------------------Show User Cart Item Testing-----------------------------

    @Test
    fun showCart() = runBlocking {
        customerRepository = CustomerRepository()


        ServiceBuilder.token =
            "Bearer " + customerRepository.validateCustomer("manish@bc.com", "manish2345").token
        val cartRepository = CartRepository()
        val response = cartRepository.getCart()
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }


    //     -----------------------------Show all Vendors testing-----------------------------
    @Test
    fun showAllVendors() = runBlocking {
        customerRepository = CustomerRepository()

        ServiceBuilder.token =
            "Bearer " + customerRepository.validateCustomer("manish@gmail.com", "manish12").token
        val vendorRepository = HomePageRepository()
        val response = vendorRepository.getVendors()
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }
    //     -----------------------------Show products of a vendor testing-----------------------------
    @Test
    fun showProducts() = runBlocking {
        customerRepository = CustomerRepository()

        ServiceBuilder.token =
            "Bearer " + customerRepository.validateCustomer("manish@bc.com", "manish2345").token
        val vendorRepository = ProductsRepository()
        val response = vendorRepository.getProducts(vendorName="vendor")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }
}