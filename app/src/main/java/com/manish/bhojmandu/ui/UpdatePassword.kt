package com.manish.bhojmandu.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.manish.bhojmandu.R
import com.manish.bhojmandu.entity.Customer
import com.manish.bhojmandu.repository.CustomerRepository
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class UpdatePassword : AppCompatActivity() {
    private lateinit var backBtn: ImageView
    private lateinit var updatePass: EditText
    private lateinit var btnUpdate: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        updatePass = findViewById(R.id.updatePass)
        backBtn = findViewById(R.id.backBtn)
        btnUpdate = findViewById(R.id.btnUpdate)

        passwordUpdateBtn()
        backImageBtn()
    }

    private fun passwordUpdateBtn(){
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }    }


    private fun backImageBtn() {
        btnUpdate.setOnClickListener {
            passwordUpdate()
        }
    }

    private fun passwordUpdate() {
        val password = updatePass.text.toString()

        val customer =
            Customer(password = password)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = CustomerRepository()
                val response = repository.updatePassword(customer)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        Toasty.success(this@UpdatePassword, "Password updated", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toasty.warning(
                            this@UpdatePassword,
                            "Error updating password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (ex: IOException) {
                withContext(Dispatchers.Main) {
                    Toasty.error(
                        this@UpdatePassword,
                        "Something is wrong ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            }
        }
    }
}