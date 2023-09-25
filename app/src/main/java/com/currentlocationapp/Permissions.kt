package com.currentlocationapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object Permissions {
	private const val PERMISSION_REQUEST_CODE = 5464

	private val requiredPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

	private const val locationPermissionCode = 2

	fun hasAllPermissions(context: Context): Boolean {
		for(p in requiredPermissions){
			if(ActivityCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED){
				return false
			}
		}

		return true
	}

	fun requestAllPermissions(activity: MainActivity){
		ActivityCompat.requestPermissions(activity, requiredPermissions, PERMISSION_REQUEST_CODE)
	}
}