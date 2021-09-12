package com.manish.bhojmandu.ui

import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PowerManager
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.manish.bhojmandu.R
import com.manish.bhojmandu.Utility.Notifications
import com.manish.bhojmandu.database.CustomerDB
import com.manish.bhojmandu.repository.CustomerRepository
import com.manish.bhojmandu.ui.fragments.CartFragment
import com.manish.bhojmandu.ui.fragments.DashBoardFragment
import com.manish.bhojmandu.ui.fragments.HomeFragment
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.NotActiveException

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var sensorsManager: SensorManager
    private lateinit var sidebarName: TextView
    private lateinit var sidebarNumber: TextView
    private lateinit var sidebarEmail: TextView
    private var navHeader: View? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sidebarAll: NavigationView
    lateinit var toggle: ActionBarDrawerToggle
    private var sensorAccelerometer: Sensor? = null
    private var sensorProximity: Sensor? = null
    private var sensorGyroscope: Sensor? = null
    private var acclValue = 0f
    private var lastAcclValue: Float = 0f
    private var shake: Float = 0f
    private var powerManager: PowerManager? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var field = 0x00000020


    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()!!.hide()
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //adapters
        fragmentContainer = findViewById(R.id.containerFragment)
        bottomNav = findViewById(R.id.bottomNav)
        drawerLayout = findViewById(R.id.drawerLayout)
        sidebarAll = findViewById(R.id.sidebarAll)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        getCutomer()

        ////sensors
        sensorsManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorAccelerometer =
            sensorsManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorProximity = sensorsManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        sensorGyroscope = sensorsManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        try {
            field = PowerManager::class.java.javaClass.getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK")
                .getInt(null)
        } catch (ignored: Throwable) {
            powerManager = getSystemService(POWER_SERVICE) as PowerManager;
            wakeLock = powerManager!!.newWakeLock(field, getLocalClassName());
        }


        // side navigation header
        navHeader = sidebarAll.getHeaderView(0)
        sidebarName = navHeader!!.findViewById<View>(R.id.sidebarName) as TextView
        sidebarNumber = navHeader!!.findViewById<View>(R.id.sidebarNumber) as TextView
        sidebarEmail = navHeader!!.findViewById<View>(R.id.sidebarEmail) as TextView


        supportFragmentManager.beginTransaction().replace(R.id.containerFragment, HomeFragment())
            .commit()
        drawerLayout.closeDrawer(GravityCompat.START)
        sidebarAll.setNavigationItemSelectedListener(this)


    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_home -> selectedFragment = HomeFragment()
                R.id.nav_cart -> selectedFragment = CartFragment()
                R.id.nav_user -> selectedFragment = DashBoardFragment()
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.containerFragment,
                    selectedFragment
                ).commit()
            }
            true
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_map ->
                startActivity(Intent(applicationContext, MapsActivity::class.java))
            R.id.nav_logout -> logout()
            R.id.aboutUs -> startActivity(Intent(applicationContext, AboutUs::class.java))
            R.id.nav_profile -> Toast.makeText(
                applicationContext,
                "Message is clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun getCutomer() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val repository = CustomerRepository()
                val response = repository.getCustomer()
                if (response.success == true) {
                    val customerData = response.data
                    CustomerDB.getInstance(this@MainActivity).getCustomerDao().deleteCustomer(
                        customerData!!
                    )
                    CustomerDB.getInstance(this@MainActivity).getCustomerDao()
                        .registerCustomer(customerData!!)
                    withContext(Dispatchers.Main) {
                        sidebarName.text = response.data.fname
                        sidebarNumber.text = response.data.number.toString()
                        sidebarEmail.text = response.data.email
                    }
                }

            }

        } catch (ex: Exception) {

            Toasty.error(
                this@MainActivity,
                "Error getting Info: ${ex.toString()}", Toast.LENGTH_SHORT
            ).show()
        }
    }

    ////sensors codes
    private fun checkSensor(): Boolean {
        var flag = true
        if (sensorsManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            flag = false
        } else if (sensorsManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null) {
            flag = false
        }
        return flag
    }

    override fun onResume() {
        super.onResume()
        sensorsManager.registerListener(
            accelerometerEventListener,
            sensorAccelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
        sensorsManager.registerListener(
            proximityEventListener,
            sensorProximity,
            SensorManager.SENSOR_DELAY_NORMAL
        )
//        sensorsManager.registerListener(
//            gyroEventListener,
//            sensorGyroscope,
//            SensorManager.SENSOR_DELAY_NORMAL
//        )

    }

    private fun logout() {
        val sharedPref = getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putString("email", "")
        editor?.putString("password", "")
        editor?.apply()
        //notification
        val notificationManager =
            NotificationManagerCompat.from(this@MainActivity)
        val notificationChannels = Notifications(this@MainActivity)
        notificationChannels.createNotificationChannels()

        val notification = NotificationCompat.Builder(
            this@MainActivity,
            notificationChannels.CHANNEL_1
        )
            .setSmallIcon(R.drawable.ic_baseline_notifications_1)
            .setContentTitle("Logout")
            .setContentText("${sidebarName.text.toString()} Logged out ")
            .setColor(Color.BLUE)
            .build()
        notificationManager.notify(1, notification)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        sensorsManager.unregisterListener(accelerometerEventListener)
        sensorsManager.unregisterListener(proximityEventListener)
        sensorsManager.unregisterListener(gyroEventListener)

    }

    private val gyroEventListener: SensorEventListener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {
            try {
                val values = event!!.values[0]
                if (values < 0)
                    drawerLayout.closeDrawer(GravityCompat.START)
                else if (values > 0)
                    drawerLayout.openDrawer(GravityCompat.START)
            } catch (e: NotActiveException) {
                Toast.makeText(this@MainActivity, "$e", Toast.LENGTH_LONG).show()
            }

        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    private val accelerometerEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcclValue = acclValue
            acclValue = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta = acclValue - lastAcclValue
            shake = shake * 0.9f + delta
            if (shake > 12) {
                logout()
                Toast.makeText(
                    this@MainActivity,
                    "Logging out",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    private val proximityEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val values = event.values[0]
            if (values <= 4) {
                if (!wakeLock?.isHeld!!) {
                    wakeLock?.acquire();
                } else {
                    if (wakeLock!!.isHeld) {
                        wakeLock!!.release();
                    }
                }
            } else {
                if (wakeLock!!.isHeld) {
                    wakeLock!!.release()
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }


}
