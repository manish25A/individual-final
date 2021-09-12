package com.manish.bhojmandu.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.manish.bhojmandu.R
import com.manish.bhojmandu.api.ServiceBuilder
import com.manish.bhojmandu.repository.CustomerRepository
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var image: ImageView
    var email: String? = null
    var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE) //will hide the title
        supportActionBar!!.hide()
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        image = findViewById(R.id.splashImage)

        if (!checkInternetConnection()) {
            Toasty.error(
                this,
                "No Internet connection , please turn on wifi or mobile data",
                Toast.LENGTH_LONG
            ).show()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                getSharedPref()
                if (email != "") {
                    getSharedPref()
                } else {
                    redirectPage()
                }
            }
        }
       getSharedPref()
    }
    private fun checkInternetConnection(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
    private fun redirectPage() {
        startActivity(
            Intent(
                this@SplashActivity,
                LoginActivity::class.java
            )
        )
        finish()
    }
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        if (sharedPref.getString("email", "")!!.isNotEmpty() && sharedPref.getString(
                "password",
                ""
            )!!.isNotEmpty()
        ) {
            val email = sharedPref.getString("email", "")
            val password = sharedPref.getString("password", "")
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = CustomerRepository()
                    val response = repository.validateCustomer(email!!, password!!)
                    if (response.success == true) {
                        ServiceBuilder.token = "Bearer ${response.token}"
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    } else {
                        reLogin()
                        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (ex: Exception) {
                    reLogin()
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                reLogin()
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun reLogin() {
        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("email", "")
        editor.putString("password", "")
        editor.apply()
    }
}
