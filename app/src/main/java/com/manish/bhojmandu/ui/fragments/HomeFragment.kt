package com.manish.bhojmandu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manish.bhojmandu.R
import com.manish.bhojmandu.database.CustomerDB
import com.manish.bhojmandu.entity.Vendors
import com.manish.bhojmandu.repository.HomePageRepository
import com.manish.bhojmandu.repository.ProductsRepository
import com.manish.bhojmandu.ui.adapter.AllVendorAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class HomeFragment : Fragment() {
    private lateinit var recyclerProduct: RecyclerView
    private var VendorList = ArrayList<Vendors>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_home, container, false
        )

        recyclerProduct = view.findViewById(R.id.recycler_restaurants)


        VendorData()

        val adapter = context?.let { AllVendorAdapter(VendorList, it) }
        val LayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerProduct.layoutManager = LayoutManager
        recyclerProduct.adapter = adapter

        return view
    }

    private fun VendorData() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val HomePageRepository = HomePageRepository()
                val response = HomePageRepository.getVendors()
                if (response.success == true) {
                    val VendorList = response.data
                    CustomerDB.getInstance(requireContext()).VendorDao().deleteVendor()
                    CustomerDB.getInstance(requireContext()).VendorDao()
                        .insertVendor(response.data)
                    withContext(Dispatchers.Main) {
                        println(response)
//                        Toast.makeText(context, "$Productitemlist", Toast.LENGTH_SHORT).show()
                        val adapter = AllVendorAdapter(
                            VendorList as ArrayList<Vendors>,
                            requireContext()
                        )
                        recyclerProduct.layoutManager = GridLayoutManager(context,2)
                        GridLayoutManager.HORIZONTAL
                        recyclerProduct.adapter = adapter
                    }
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(
                context,
                "Error : ${ex.toString()}", Toast.LENGTH_SHORT
            ).show()
        }
    }


}