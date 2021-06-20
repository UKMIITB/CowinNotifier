package com.example.cowinnotifier.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.cowinnotifier.R
import com.example.cowinnotifier.helper.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        createNotificationChannel()
    }

    private fun init() {
        setLocationRadioGroupClickListener()
    }

    private fun setLocationRadioGroupClickListener() {
        radio_group_location_type.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_location_pincode -> {
//                    showPincodeFragment()
                }
                R.id.radio_button_location_district -> {
                    showDistrictFragment()
                }
                R.id.radio_button_location_gps -> {
//                    showGpsFragment()
                }
            }
        }
    }

    private fun showDistrictFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, DistrictSearchFragment())
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = AppConstants.NOTIFICATION_CHANNEL_NAME
            val descriptionText = AppConstants.NOTIFICATION_CHANNEL_DESC
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(AppConstants.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.how_to_use -> {
                howToUseClicked()
                true
            }
            R.id.current_search -> {
                currentSearchClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun howToUseClicked() {
        startActivity(Intent(this, HowToUseActivity::class.java))
    }

    private fun currentSearchClicked() {
        startActivity(Intent(this, CurrentSearchActivity::class.java))
    }
}
