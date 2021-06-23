package com.example.cowinnotifier.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.commit
import com.example.cowinnotifier.R
import com.example.cowinnotifier.helper.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val PINCODE_FRAGMENT = 0
    private val DISTRICT_FRAGMENT = 1
    private val GPS_FRAGMENT = 2
    private val NOTIFICATION_ON = "On"
    private val NOTIFICATION_OFF = "Off"

    private val pincodeSearchFragment = PincodeSearchFragment()
    private val districtSearchFragment = DistrictSearchFragment()

    private val vaccineSelected = mutableListOf<String>()
    private var currentActiveFragment = PINCODE_FRAGMENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        createNotificationChannel()
    }

    private fun init() {
        showFragment(PINCODE_FRAGMENT)
        setLocationRadioGroupClickListener()
        showResultButtonClickListener()
        notificationButtonClickListener()
    }

    private fun setLocationRadioGroupClickListener() {
        radio_group_location_type.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_location_pincode -> {
                    showFragment(PINCODE_FRAGMENT)
                    currentActiveFragment = PINCODE_FRAGMENT
                }
                R.id.radio_button_location_district -> {
                    showFragment(DISTRICT_FRAGMENT)
                    currentActiveFragment = DISTRICT_FRAGMENT
                }
                R.id.radio_button_location_gps -> {
                    showFragment(GPS_FRAGMENT)
                    currentActiveFragment = GPS_FRAGMENT
                }
                else -> {
                    showFragment(PINCODE_FRAGMENT)
                    currentActiveFragment = PINCODE_FRAGMENT
                }
            }
        }
    }

    fun onVaccineCheckboxClicked(view: View) { // method call set as onClick parameter in xml
        if (view is CheckBox) {
            val checked = view.isChecked

            when (view.id) {
                R.id.checkbox_covaxin -> {
                    updateVaccineSelectedList(checked, view.text.toString())
                }
                R.id.checkbox_covishield -> {
                    updateVaccineSelectedList(checked, view.text.toString())
                }
                R.id.checkbox_sputnik -> {
                    updateVaccineSelectedList(checked, view.text.toString())
                }
            }
        }
    }

    private fun updateVaccineSelectedList(checked: Boolean, vaccineName: String) {
        if (checked) {
            vaccineSelected.add(AppConstants.VACCINE_FILTER_MAP[vaccineName]!!)
        } else {
            vaccineSelected.remove(AppConstants.VACCINE_FILTER_MAP[vaccineName]!!)
        }
        if (vaccineSelected.size == 0) {
            button_show_results.isEnabled = false
            button_show_results.background = ResourcesCompat.getDrawable(resources, R.drawable.button_rounded_corners_disabled, null)
        } else {
            button_show_results.isEnabled = true
            button_show_results.background = ResourcesCompat.getDrawable(resources, R.drawable.button_rounded_corners_enabled, null)
        }
    }

    private fun showFragment(code: Int) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            when (code) {
                PINCODE_FRAGMENT -> replace(R.id.fragment_container, pincodeSearchFragment)
                DISTRICT_FRAGMENT -> replace(R.id.fragment_container, districtSearchFragment)
                GPS_FRAGMENT -> print("GPS Fragment opening")//todo start gps fragment
            }
        }
    }

    private fun showResultButtonClickListener() {
        button_show_results.setOnClickListener {

        }
    }

    private fun notificationButtonClickListener() {
        imageview_notification_button.setOnClickListener {
            if (textview_notification_status.text.equals(NOTIFICATION_ON)) {
                textview_notification_status.text = NOTIFICATION_OFF
                imageview_notification_button.setImageResource(R.drawable.notification_off)
            } else {
                textview_notification_status.text = NOTIFICATION_ON
                imageview_notification_button.setImageResource(R.drawable.notification_on)
            }
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
