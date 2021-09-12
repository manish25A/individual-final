package com.manish.bhojmandu.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.manish.bhojmandu.R
import com.manish.bhojmandu.entity.ProductsModel
import com.manish.bhojmandu.repository.CartRepository
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ProductsAdapter(

    private val products: ArrayList<ProductsModel>,
    val context: Context,
    private var _id: String = ""
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.itemName)
        val price: TextView = view.findViewById(R.id.price)
        val desc: TextView = view.findViewById(R.id.description)
        val addToCart: MaterialButton = view.findViewById(R.id.addToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_products_display, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.name.text = product.name
        holder.price.text = product.price.toString()
        holder.desc.text = product.desc


        holder.addToCart.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = CartRepository()
                    val response = repository.insertCart(product._id!!)
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            Toasty.success(
                                context, "Item Added Successfully To Cart",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toasty.error(context, "Error Adding", Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                } catch (ex: IOException) {
                    withContext(Dispatchers.Main) {
                        Toasty.error(context,"$ex",Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }


}
