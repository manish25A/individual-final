package com.manish.bhojmandu.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.manish.bhojmandu.R
import com.manish.bhojmandu.api.ServiceBuilder
import com.manish.bhojmandu.repository.CustomerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var etemail: EditText
    private lateinit var etpassword: EditText
    private lateinit var signup: TextView
    private lateinit var btnLogIn: Button
    private lateinit var linearLayout: LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()!!.hide();
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        etemail = findViewById(R.id.email)
        etpassword = findViewById(R.id.password)
        signup = findViewById(R.id.signup)

        btnLogIn = findViewById(R.id.la_btn_login)

        btnLogIn()
        tvSignup()

    }

    private fun tvSignup() {
        signup.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    SignupActivity::class.java
                )
            )
        }
    }

    private fun btnLogIn() {
        btnLogIn.setOnClickListener {
            if (inputValidation()) {
                customerLogin()
                saveSharedPref()

            }
        }
    }


    private fun saveSharedPref() {
        val email = etemail.text.toString().trim()
        val password = etpassword.text.toString().trim()

        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    fun customerLogin() {
        val email = etemail.text.toString().trim()
        val password = etpassword.text.toString().trim()
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val repository = CustomerRepository()
                val response = repository.validateCustomer(email, password)
                if (response.success == true) {
                    ServiceBuilder.token = "Bearer ${response.token}"
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            MainActivity::class.java
                        )
                    )
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        val snack =
                            Snackbar.make(
                                linearLayout,
                                "Invalid credentials",
                                Snackbar.LENGTH_LONG
                            )
                        snack.setAction("OK", View.OnClickListener {
                            snack.dismiss()
                        })
                        snack.show()
                    }
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login error", Toast.LENGTH_SHORT
                    ).show()
                }
            }
            clearLoginTextfields()
        }
    }


    private fun inputValidation(): Boolean {
        var valid = true
        when {
            TextUtils.isEmpty(etemail.text.toString()) -> {
                etemail.error = "Email Must Be Fulfilled "
                etemail.requestFocus()
                valid = false
            }
            TextUtils.isEmpty(etpassword.text.toString()) -> {
                etpassword.error = "Password Must Be Fulfilled"
                etpassword.requestFocus()
                valid = false
            }
        }
        return valid
    }

    fun clearLoginTextfields() {
        return
    }
}
