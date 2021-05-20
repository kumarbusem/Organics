package com.tekkr.organics.features.splash

import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tekkr.organics.BuildConfig.APPLICATION_ID
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseFragment
import com.tekkr.organics.common.PermissionManager
import com.tekkr.organics.common.PermissionManager.Companion.PERMISSION_REQUEST_CODE
import com.tekkr.organics.common.PermissionManager.Companion.REQUEST_PERMISSIONS_REQUEST_CODE
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateNext()
    }

    private fun navigateNext() {

        uiScope.launch {
                delay(SPLASH_DURATION)
                checkUserAuth()
            }
    }

    private fun checkUserAuth() {
        val user = repoPrefs.getLoggedInUser()
        Log.e("CHECK USE AUTH", user.toString())
        if (user == null || user.token.isNullOrEmpty()) {
            navigateById(R.id.action_splashFragment_to_loginFragment)
        } else {
            navigateById(R.id.action_splashFragment_to_loginFragment)
        }
    }

    companion object {
        private const val SPLASH_DURATION: Long = 2000
    }

}
