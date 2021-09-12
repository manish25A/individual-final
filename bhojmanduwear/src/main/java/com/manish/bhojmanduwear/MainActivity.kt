package com.manish.bhojmanduwear

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.manish.bhojmandu.api.ServiceBuilder
import com.manish.bhojmandu.repository.CustomerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : WearableActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText

    private lateinit var loginbtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginbtn = findViewById(R.id.loginBtn)

        loginbtn.setOnClickListener{
            btnLogIn()
            inputValidation()
        }


        // Enables Always-on
        setAmbientEnabled()
    }

    private fun btnLogIn() {

        val email = email.text.toString().trim()
        val password = password.text.toString().trim()
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val repository = CustomerRepository()
                val response = repository.validateCustomer(email, password)
                if (response.success == true) {
                    ServiceBuilder.token = "Bearer ${response.token}"
                    startActivity(
                        Intent(
                            this@MainActivity,
                            CartActivity::class.java
                        )
                    )
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Login error no token", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Login error", Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
    private fun inputValidation(): Boolean {
        var valid = true
        when {
            TextUtils.isEmpty(email.text.toString()) -> {
                email.error = "Email Must Be Fulfilled "
                email.requestFocus()
                valid = false
            }
            TextUtils.isEmpty(password.text.toString()) -> {
                password.error = "Password Must Be Fulfilled"
                password.requestFocus()
                valid = false
            }
        }
        return valid
    }
}
