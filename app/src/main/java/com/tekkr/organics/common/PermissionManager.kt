package com.tekkr.organics.common

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment

class PermissionManager(private val host: Fragment) {

    fun areAllPermissionsGranted(): Boolean = checkPermissions()

    fun requestAllPermissions() {
        takePermissions()

    }

    fun checkLocationPermissions(): Boolean {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            PackageManager.PERMISSION_GRANTED == checkSelfPermission(host.requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        else PackageManager.PERMISSION_GRANTED == checkSelfPermission(host.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun checkPermissions(): Boolean {
        return !(checkSelfPermission(host.requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(host.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(host.requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(host.requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(host.requireContext(), Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(host.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(host.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }

    private fun takePermissions() {
        host.requestPermissions(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE)
    }

    fun takeLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            host.requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE)
        else
            host.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    companion object {

        const val PERMISSION_REQUEST_CODE: Int = 100
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
}