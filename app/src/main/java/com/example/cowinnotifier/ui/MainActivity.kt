package com.example.cowinnotifier.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cowinnotifier.R
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.ui.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        createNotificationChannel()
    }

    private fun init() {
        viewPagerAdapter = ViewPagerAdapter(this)
        viewpager.adapter = viewPagerAdapter
        TabLayoutMediator(tab_layout, viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "Search by Pin"
                1 -> tab.text = "Search by District"
            }
        }.attach()
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
}
