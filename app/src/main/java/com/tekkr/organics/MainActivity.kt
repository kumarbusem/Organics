package com.tekkr.organics

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tekkr.data.dataSources.definitions.DataSourceFirestore
import com.tekkr.data.dataSources.definitions.DataSourceSharedPreferences
import com.tekkr.data.dataSources.repos.RepoFirestore
import com.tekkr.data.dataSources.repos.RepoSharedPreferences
import com.tekkr.organics.common.*
import com.tekkr.organics.location.LocationService
import com.tekkr.organics.location.LocationSocket
import com.tekkr.organics.location.Util
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.partial_blocked_version.view.*

class MainActivity : AppCompatActivity() {

    var mLocationService: LocationService = LocationService()
    lateinit var mServiceIntent: Intent
    lateinit var mActivity: Activity

    protected val repoFirestore: DataSourceFirestore by lazy { RepoFirestore() }
    protected val repoPrefs: DataSourceSharedPreferences by lazy { RepoSharedPreferences() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLocationTracking()
        getAppSettings()

        setAppUpdteButtons()

        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/WorkSans-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build())
                ).build())

        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun initLocationTracking() {
        mActivity = this@MainActivity

        if (!Util.isLocationEnabledOrNot(mActivity)) {
            Util.showAlertLocation(
                    mActivity,
                    getString(R.string.gps_enable),
                    getString(R.string.please_turn_on_gps),
                    getString(
                            R.string.ok
                    )
            )
        }

        val socket = LocationSocket()
        socket.createSocket()
        val riderId: String = repoPrefs.getLoggedInUser()?.sf_id.toString()
        socket.joinRoom(riderId)

    }

    fun startLocationService() {
        mLocationService = LocationService()
        mServiceIntent = Intent(this, mLocationService.javaClass)
        if (!Util.isMyServiceRunning(mLocationService.javaClass, mActivity)) {
            startService(mServiceIntent)
            Log.i("INFO", getString(R.string.service_start_successfully))
        } else {
            Log.i("INFO", getString(R.string.service_already_running))
        }
    }

    private fun getAppSettings() {

        repoFirestore.getAppSettings { settings ->
            Log.e("APP SETTINGS::", settings.toString())
            if (settings == null) return@getAppSettings

            if (settings.blockVersionUpTo!! > BuildConfig.VERSION_NAME) {
                plBlockedVersion.show()
                plBlockedVersion.tvHeading.text = "Unsupported App Version\n${BuildConfig.VERSION_NAME}"

            } else {
                plBlockedVersion.hide()
            }

            if (settings.underMaintenance == true) {
                plUnderMaintenance.show()
            } else {
                plUnderMaintenance.hide()
            }

        }
    }


    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isNetworkAvailable(this@MainActivity)) plNoInternet.hide()
            else plNoInternet.show()
        }
    }



    override fun onStart() {
        super.onStart()
        val networkIntentFilter = IntentFilter(CONNECTION)
        val gpsIntentFilter = IntentFilter(GPS)
        this.registerReceiver(networkReceiver, networkIntentFilter)

        if (isGpsAvailable(this@MainActivity)) plNoGPS.hide()
        else plNoGPS.show()
        if (isNetworkAvailable(this@MainActivity)) plNoInternet.hide()
        else plNoInternet.show()
    }

    override fun onStop() {
        super.onStop()
        this.unregisterReceiver(networkReceiver)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    companion object {
        const val CONNECTION = "android.net.conn.CONNECTIVITY_CHANGE"
        const val GPS = "android.location.PROVIDERS_CHANGED"
    }

    private fun setAppUpdteButtons() {
        val downloadUrl = "https://drive.google.com/file/d/1BLsnDUxep_wVvKKQegzFUGQTA_RWy9zv/view?usp=sharing"
        plBlockedVersion.btnUpdatePlaystore.setOnClickListener {
            val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(downloadUrl)
            )
            startActivity(browserIntent)
        }
        plBlockedVersion.btnUpdate.setOnClickListener {
            val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(downloadUrl)
            )
            startActivity(browserIntent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        /*STOP SERVICE WHERE APPS KILL*/
        if (::mServiceIntent.isInitialized) {
            stopService(mServiceIntent)
        }
        /*STOP SERVICE WHERE APPS KILL*/
    }
}