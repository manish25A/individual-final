package com.manish.bhojmanduwear

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.manish.bhojmandu.repository.CartRepository
import com.manish.bhojmandu.repository.CustomerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : WearableActivity() {
    private lateinit var itemName:TextView
    private lateinit var checkOut:ImageView
    private lateinit var price:TextView
    private lateinit var vendorName:TextView
    private lateinit var description:TextView
    private lateinit var deleteImage:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        itemName= findViewById(R.id.itemName)
        checkOut= findViewById(R.id.checkOut)
        price= findViewById(R.id.price)
        vendorName= findViewById(R.id.vendorName)
        description= findViewById(R.id.description)
        deleteImage= findViewById(R.id.deleteImage)
        getCustomer()
    }

    private fun getCustomer() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val repository = CartRepository()
                val response = repository.getCart()
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        vendorName.setText(response.data?.get(0)?.itemId?.get(0)?.vendorName)
                        itemName.setText(response.data?.get(0)?.itemId?.get(0)?.name)
                        price.setText(response.data?.get(0)?.itemId?.get(0)?.price)

                    }
                }
            }
        } catch (ex: Exception) {

            Toast.makeText(
                this@CartActivity,
                "Error here man: ${ex.toString()}", Toast.LENGTH_SHORT
            ).show()
        }
    }

}
