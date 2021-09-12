package com.manish.bhojmandu.ui

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manish.bhojmandu.R
import com.manish.bhojmandu.database.CustomerDB
import com.manish.bhojmandu.entity.ProductsModel
import com.manish.bhojmandu.repository.ProductsRepository
import com.manish.bhojmandu.ui.adapter.ProductsAdapter
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDisplayActivity : AppCompatActivity() {
    private var vendorName: String = ""
    private var id: String = ""
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var vendorRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar!!.hide();
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_display)
        productsRecyclerView = findViewById(R.id.productDisplay)
        vendorName = intent.getStringExtra("vendorName").toString()

        loadProducts()
        loadVendor()
    }
    private fun loadVendor(){
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val productsRepository = ProductsRepository()
                val response =
                    productsRepository.getProducts(intent.getStringExtra("vendorName").toString())
                if (response.success == true) {
                    val recentitemlist = response.data
                    CustomerDB.getInstance(this@ProductDisplayActivity).ProductsDao()
                        .deleteProduct()
                    CustomerDB.getInstance(this@ProductDisplayActivity).ProductsDao()
                        .insertProduct(recentitemlist as ArrayList)
                    withContext(Main) {
                        val adapter = ProductsAdapter(
                            recentitemlist as ArrayList<ProductsModel>,
                            this@ProductDisplayActivity
                        )
                        productsRecyclerView.layoutManager =
                            LinearLayoutManager(this@ProductDisplayActivity)
                        productsRecyclerView.adapter = adapter
                    }
                }
            }
        } catch (ex: Exception) {
            Toasty.error(
                this@ProductDisplayActivity,
                "Error : ${ex.toString()}", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadProducts() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val productsRepository = ProductsRepository()
                val response =
                    productsRepository.getProducts(intent.getStringExtra("vendorName").toString())
                if (response.success == true) {
                    val recentitemlist = response.data
                    CustomerDB.getInstance(this@ProductDisplayActivity).ProductsDao()
                        .deleteProduct()
                    CustomerDB.getInstance(this@ProductDisplayActivity).ProductsDao()
                        .insertProduct(recentitemlist as ArrayList)
                    withContext(Main) {
                        val adapter = ProductsAdapter(
                            recentitemlist as ArrayList<ProductsModel>,
                            this@ProductDisplayActivity
                        )

                        productsRecyclerView.layoutManager =
                            LinearLayoutManager(this@ProductDisplayActivity)
                        productsRecyclerView.adapter = adapter
                    }
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(
                this@ProductDisplayActivity,
                "Error : ${ex.toString()}", Toast.LENGTH_SHORT
            ).show()
        }
    }
}