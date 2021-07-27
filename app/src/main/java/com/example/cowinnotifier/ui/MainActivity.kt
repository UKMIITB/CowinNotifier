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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.commit
import com.example.cowinnotifier.R
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.model.SearchParameter
import com.example.cowinnotifier.viewmodel.ActivityViewModel
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
    private val locationSearchFragment = LocationSearchFragment()

    private val vaccineSelected = mutableListOf<String>()
    private var currentActiveFragment = PINCODE_FRAGMENT
    private var doseSelected = AppConstants.DOSE_FILTER_MAP["1st"]
    private var minAgeSelected: Long = 18

    private val viewmodel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        createNotificationChannel()
    }

    private fun init() {
        showFragment(PINCODE_FRAGMENT)
        locationRadioGroupClickListener()
        doseRadioGroupClickListener()
        ageRadioGroupClickListener()
        showResultButtonClickListener()
        notificationButtonClickListener()
    }

    private fun locationRadioGroupClickListener() {
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
            }
        }
    }

    private fun doseRadioGroupClickListener() {
        radio_group_dose.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_first_dose -> {
                    doseSelected =
                        AppConstants.DOSE_FILTER_MAP[radio_button_first_dose.text.toString()]
                }
                R.id.radio_button_second_dose -> {
                    doseSelected =
                        AppConstants.DOSE_FILTER_MAP[radio_button_second_dose.text.toString()]
                }
            }
        }
    }

    private fun ageRadioGroupClickListener() {
        radio_group_age.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_18_45_age -> {
                    minAgeSelected = 18
                }
                R.id.radio_button_45_plus_age -> {
                    minAgeSelected = 45
                }
            }
        }
    }

    fun onVaccineCheckboxClicked(view: View) { // method call set as onClick parameter in xml
        if (view is CheckBox) {
            val checked = view.isChecked
            updateVaccineSelectedList(checked, view.text.toString())
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
            button_show_results.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.button_rounded_corners_disabled,
                null
            )
        } else {
            button_show_results.isEnabled = true
            button_show_results.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.button_rounded_corners_enabled,
                null
            )
        }
    }

    private fun showFragment(code: Int) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            when (code) {
                PINCODE_FRAGMENT -> replace(R.id.fragment_container, pincodeSearchFragment)
                DISTRICT_FRAGMENT -> replace(R.id.fragment_container, districtSearchFragment)
                GPS_FRAGMENT -> replace(R.id.fragment_container, locationSearchFragment)
            }
        }
    }

    private fun showResultButtonClickListener() {
        button_show_results.setOnClickListener {
            when (currentActiveFragment) {
                PINCODE_FRAGMENT -> {
                    searchByPincode()
                }
                DISTRICT_FRAGMENT -> {
                    searchByDistrict()
                }
                GPS_FRAGMENT -> {

                }
            }
        }
    }

    private fun searchByPincode() {
        val pincode = pincodeSearchFragment.getPincode().trim()
        if (pincode.length != 6) {
            Toast.makeText(this, "Please enter a valid Pincode", Toast.LENGTH_SHORT).show()
            return
        }
        startSearch(AppConstants.SEARCH_BY_PINCODE, pincode)
    }

    private fun searchByDistrict() {
        val districtId = districtSearchFragment.getSelectedDistrictId()
        startSearch(AppConstants.SEARCH_BY_DISTRICT, districtId)
    }

    private fun startSearch(key: String, value: String) {
        viewmodel.insertSearchParameter(
            SearchParameter(
                1,
                getVaccineSelectedList(),
                doseSelected!!,
                minAgeSelected,
                key,
                value
            )
        )
        viewmodel.startActivityFromIntent(
            key,
            value,
            this,
            minAgeSelected,
            doseSelected!!,
            getVaccineSelectedList()
        )
    }

    private fun getVaccineSelectedList(): ArrayList<String> {
        val vaccineList = arrayListOf<String>()
        for (vaccine in vaccineSelected) {
            vaccineList.add(vaccine)
        }
        return vaccineList
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
