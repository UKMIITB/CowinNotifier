package com.example.cowinnotifier.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.cowinnotifier.R
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_location_search.*
import java.util.*


class LocationSearchFragment : Fragment() {

    private val PERMISSION_REQUEST_CODE = 100
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        button_location.setOnClickListener {
            if (isLocationPermissionGranted()) {
                getCurrentLocation()
            }
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_REQUEST_CODE
            )
        }
        return false
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        ) {
            setProgressBarVisibility(View.VISIBLE)
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location = it.result
                location?.apply {
                    updateAddressUI(this.latitude, this.longitude)
                } ?: run {
                    val locationRequest = LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10000)
                        .setFastestInterval(1000)
                        .setNumUpdates(1)

                    val locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            updateAddressUI(
                                locationResult.lastLocation.latitude,
                                locationResult.lastLocation.longitude
                            )
                        }
                    }
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )
                }
            }
        } else {
            startActivity(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED &&
            grantResults[1] == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateAddressUI(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
        val city: String = addresses[0].locality
        val state: String = addresses[0].adminArea

        button_location.visibility = View.GONE

        val displayAddress = "$city, $state"
        tvAddress.text = displayAddress
        tvAddress.visibility = View.VISIBLE

        setProgressBarVisibility(View.GONE)
    }

    private fun setProgressBarVisibility(visibility: Int) {
        progressBar.visibility = visibility
    }
}