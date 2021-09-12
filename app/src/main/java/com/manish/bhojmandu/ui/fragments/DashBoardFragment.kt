package com.manish.bhojmandu.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.room.Update
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.manish.bhojmandu.R
import com.manish.bhojmandu.database.CustomerDB
import com.manish.bhojmandu.entity.Customer
import com.manish.bhojmandu.repository.CustomerRepository
import com.manish.bhojmandu.ui.ProductDisplayActivity
import com.manish.bhojmandu.ui.SignupActivity
import com.manish.bhojmandu.ui.UpdatePassword
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class DashBoardFragment : Fragment() {

    private lateinit var tvRegister: TextView
    private lateinit var customerName: TextView
    private lateinit var customerEmail: TextView
    private lateinit var customerNumber: TextView
    var email: String? = null
    private lateinit var eteditnumb: TextInputEditText
    private lateinit var btnupdate: Button
    private lateinit var editPassword: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        tvRegister = view.findViewById(R.id.tvRegister)
        customerName = view.findViewById(R.id.userName)
        customerEmail = view.findViewById(R.id.email)
        customerNumber = view.findViewById(R.id.customerNumber)
        eteditnumb = view.findViewById(R.id.editNumber)

        btnupdate = view.findViewById(R.id.btnupdate)
        editPassword = view.findViewById(R.id.EditPassword)

        editPassword()
        getCustomer()
        btnupdate()
        return view
    }

    fun editPassword(){
        editPassword.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    UpdatePassword::class.java
                )
            )
        }

    }
    fun btnupdate() {
        btnupdate.setOnClickListener {
            updateinfo()
        }
    }

    fun updateinfo() {
        val etNumber = eteditnumb.text.toString()


        val customer =
            Customer(
                number = etNumber
            )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = CustomerRepository()
                val response = repository.updateCustomer(customer)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Error updating user",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (ex: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error uploading ", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }

    }


    private fun getCustomer() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val repository = CustomerRepository()
                val response = repository.getCustomer()
                if (response.success == true) {
                    val customerData = response.data
                    CustomerDB.getInstance(requireContext()).getCustomerDao()
                        .deleteCustomer(customerData!!)
                    CustomerDB.getInstance(requireContext()).getCustomerDao()
                        .registerCustomer(customerData!!)
                    withContext(Dispatchers.Main) {
                        customerName.setText(response.data?.fname)
                        customerEmail.setText(response.data?.email)
                        customerNumber.setText(response.data?.number)
                    }
                }
            }
        } catch (ex: Exception) {

            Toasty.error(
                requireContext(),
                "Error is: ${ex.toString()}", Toast.LENGTH_SHORT
            ).show()
        }
    }
}
