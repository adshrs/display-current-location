package com.currentlocationapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog

class LocationService(private val context: Context) {

	private lateinit var locationManager: LocationManager
	var callback: Callback? = null
	private lateinit var builder: AlertDialog.Builder

	@SuppressLint("MissingPermission")
	fun getCurrentLocation() {

		locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

		if (!Permissions.hasAllPermissions(context)) return

		if(!hasLocationEnabled()) {
			builder = AlertDialog.Builder(context)

			builder.setTitle("Turn on Location")
				.setMessage("Location Services are turned off. Please turn on Location from your device settings.")
				.setCancelable(false)
				.setPositiveButton("GO TO SETTINGS") { _, _ ->
					context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
				}
				.setNegativeButton("NO THANKS") { dialog, _ ->
					dialog.dismiss()
				}
				.show()
		}

		locationManager.requestLocationUpdates(
			LocationManager.GPS_PROVIDER,
			1000,
			1f,
			gpsLocationListener
		)
	}

	private val gpsLocationListener: LocationListener = object : LocationListener {
		override fun onLocationChanged(location: Location) {
			callback?.onLocationUpdated(location)
		}

		@Deprecated("Deprecated in Java")
		override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
		}

		override fun onProviderEnabled(provider: String) {}
		override fun onProviderDisabled(provider: String) {}
	}

	interface Callback {
		fun onLocationUpdated(loc: Location)
	}

	private fun hasLocationEnabled(): Boolean {
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true
		}
		return false
	}
}