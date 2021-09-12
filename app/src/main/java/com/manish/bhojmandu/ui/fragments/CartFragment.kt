package com.manish.bhojmandu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manish.bhojmandu.R
import com.manish.bhojmandu.database.CustomerDB
import com.manish.bhojmandu.entity.CartItem
import com.manish.bhojmandu.repository.CartRepository
import com.manish.bhojmandu.ui.adapter.CartItemAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalPrice:TextView
    private var cartItemlist = ArrayList<CartItem>()

    fun oncCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)


        cartItems()
        val adapter = context?.let { CartItemAdapter(cartItemlist, it) }
        recyclerView.layoutManager = LinearLayoutManager(null)

        recyclerView.adapter = adapter
        return view

    }



    private fun cartItems() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val cartRepository = CartRepository()
                val response = cartRepository.getCart()
                println(response.data)
                if (response.success == true) {
                    val cartItemList = response.data
                    println("this is $cartItemList")
                    CustomerDB.getInstance(requireContext()).CartDao().deleteCartItem()
                    CustomerDB.getInstance(requireContext()).CartDao().insertCart(cartItemList as List<CartItem>)

                    //       BuyerDb.getInstance(requireContext()).getFavouriteItemDAO().insertFavouriteItem(response.message)
                    withContext(Dispatchers.Main) {
                        println(response)
                        val adapter = CartItemAdapter(
                            cartItemList as ArrayList<CartItem>,
                            requireContext()
                        )
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.adapter = adapter
                    }
                } else {

                }
            }
        } catch (ex: Exception) {
            Toast.makeText(
                context,
                "Error : ${ex.toString()}", Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun setTotalPrice(total:Double){
            totalPrice.text=total.toString()
    }

}