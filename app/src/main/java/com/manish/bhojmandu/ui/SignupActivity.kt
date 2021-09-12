package com.manish.bhojmandu.ui

//import com.manish.bhojmandu.database.CustomerDB
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.manish.bhojmandu.R
import com.manish.bhojmandu.Utility.Notifications
import com.manish.bhojmandu.entity.Customer
import com.manish.bhojmandu.repository.CustomerRepository
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class SignupActivity : AppCompatActivity() {

    private lateinit var regText: TextView
    private lateinit var fname: EditText
    private lateinit var lname: EditText
    private lateinit var email: EditText
    private lateinit var number: EditText
    private lateinit var password: EditText
    private lateinit var regBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()!!.hide();
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        regText = findViewById(R.id.tvRegister)
        fname = findViewById(R.id.etFName)
        lname = findViewById(R.id.etLName)
        email = findViewById(R.id.etEmail)
        number = findViewById(R.id.etNumber)
        password = findViewById(R.id.etPass)
        regBtn = findViewById(R.id.btnregister)

        regBtn.setOnClickListener {
            if (!inputValidation()) {
                Toasty.error(this@SignupActivity, "Please enter valid data only")
            } else {
                customerSignup()
                val notificationManager =
                    NotificationManagerCompat.from(this@SignupActivity)
                val notificationChannels = Notifications(this@SignupActivity)
                notificationChannels.createNotificationChannels()

                val notification = NotificationCompat.Builder(
                    this@SignupActivity,
                    notificationChannels.CHANNEL_1
                )
                    .setSmallIcon(R.drawable.ic_baseline_notifications_1)
                    .setContentTitle("Register Successful")
                    .setContentText("Registered Successfully")
                    .setColor(Color.BLUE)
                    .build()
                notificationManager.notify(1, notification)
            }


        }

    }


    fun customerSignup() {
        try {
            val fname = fname.text.toString().trim()
            val email = email.text.toString().trim()
            val lname = lname.text.toString().trim()
            val password = password.text.toString().trim()
            val number = number.text.toString()


            val customerRegister = Customer(
                fname = fname,
                lname = lname,
                email = email,
                password = password,
                number = number
            )
            CoroutineScope(Dispatchers.IO).launch {
                val repository = CustomerRepository()
                val response = repository.registerCustomer(customerRegister)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        Toasty.success(
                            this@SignupActivity,
                            "Successfully registered customer",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(
                            Intent(
                                this@SignupActivity,
                                LoginActivity::class.java
                            )
                        )
                        clearTextfields()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toasty.error(
                            this@SignupActivity,
                            "Error Registering Customer",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }


        } catch (e: IOException) {
            Toast.makeText(this, "Error ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun inputValidation(): Boolean {

        var valid = true
        when {
            TextUtils.isEmpty(fname.text.toString()) -> {
                fname.error = "Please enter firstname "
                fname.requestFocus()
                valid = false
            }
            TextUtils.isEmpty(password.text.toString()) -> {
                password.error = "Password Must Be Fulfilled"
                password.requestFocus()
                valid = false
            }
            TextUtils.isEmpty(lname.text.toString()) -> {
                lname.error = "Please enter last Name"
                lname.requestFocus()
                valid = false
            }
            TextUtils.isEmpty(email.text.toString()) -> {
                email.error = "Please enter email"
                email.requestFocus()
                valid = false
            }
            TextUtils.isEmpty(number.text.toString()) -> {
                number.error = "Please enter number"
                number.requestFocus()
                valid = false
                return Patterns.PHONE.matcher(number.text.toString()).matches()
            }

        }

        return valid
    }

    fun clearTextfields() {
        fname.setText("")
        lname.setText("")
        email.setText("")
        number.setText("")
        password.setText("")
    }
}