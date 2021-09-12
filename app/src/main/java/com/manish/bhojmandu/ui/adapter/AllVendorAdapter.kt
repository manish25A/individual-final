package com.manish.bhojmandu.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.bhojmandu.R
import com.manish.bhojmandu.api.ServiceBuilder
import com.manish.bhojmandu.entity.Vendors
import com.manish.bhojmandu.repository.ProductsRepository
import com.manish.bhojmandu.ui.ProductDisplayActivity
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class AllVendorAdapter(
    val VendorList: ArrayList<Vendors>,
    val context: Context,
    private val vendorName: String = ""
) : RecyclerView.Adapter<AllVendorAdapter.ProductViewHolder>() {
    var vendorNameForIntent: String = ""

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vendorName: TextView
        val vendorAddress: TextView
        val vendorImg: ImageView


        init {
            vendorImg = view.findViewById(R.id.vendorImage)
            vendorName = view.findViewById(R.id.vendorName)
            vendorAddress = view.findViewById(R.id.vendorAddress)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.getvendors, parent, false)
        return ProductViewHolder(view)

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val vendor = VendorList[position]
        val imagePath = ServiceBuilder.loadImagePath() + vendor.photo

        if (!vendor.photo.equals(null)) {
            Glide.with(context)
                .load(imagePath)
                .fitCenter()
                .into(holder.vendorImg)
        }
        holder.vendorName.text = vendor.vendorName
        holder.vendorAddress.text = vendor.vendorAddress

        holder.vendorImg.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = ProductsRepository()
                    val response = repository.getProducts(vendor.vendorName!!)
                    if (response.success == true) {

                        val intent = Intent(context, ProductDisplayActivity::class.java)
                        intent.putExtra("vendorName", vendor.vendorName)
                        context.startActivity(intent)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toasty.error(context, "Error Adding", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } catch (ex: IOException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, ex.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
//
        }
    }

    override fun getItemCount(): Int {
        return VendorList.size
    }
}