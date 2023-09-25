package com.currentlocationapp

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.currentlocationapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), LocationService.Callback {

    private lateinit var binding: ActivityMainBinding
    private val locService = LocationService(this)
    private lateinit var builder: AlertDialog.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(Permissions.hasAllPermissions(this)){
            initApp()
        }
        else{
            Permissions.requestAllPermissions(this)
        }
    }

    private fun initApp(){
        locService.callback = this

        binding.btnGetLocation.setOnClickListener {
            locService.getCurrentLocation()
        }
    }

    override fun onLocationUpdated(loc: Location) {
        binding.tvLatitude.text = "${loc.latitude}"
        binding.tvLongitude.text = "${loc.longitude}"
        binding.tvAccuracy.text = loc.accuracy.toString()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var isAllGranted = true
        for(r in grantResults){
            if (r!=PackageManager.PERMISSION_GRANTED){
                isAllGranted = false
            }
        }
        if(isAllGranted){
            initApp()
        }
        else{
            //Show dialog to retry getting permissions
            builder = AlertDialog.Builder(this)

            builder.setTitle("Permissions Required")
                .setMessage("Required permissions are not allowed, please retry.")
                .setCancelable(false)
                .setPositiveButton("RETRY") { _, _ ->
                    Permissions.requestAllPermissions(this)
                }
                .setNegativeButton("EXIT") { _, _ ->
                    finish()
                }
                .show()
        }
    }
}

