package com.tekkr.organics.location

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.tekkr.data.dataSources.definitions.DataSourceSharedPreferences
import com.tekkr.data.dataSources.repos.RepoSharedPreferences
import com.tekkr.organics.R
import java.util.*

class LocationService : Service() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    protected val repoPrefs: DataSourceSharedPreferences by lazy { RepoSharedPreferences() }
    var socket = LocationSocket()

    override fun onBind(intent: Intent): IBinder? {

        // Register Firestore when service will restart
        //todo get rider id here
        socket.createSocket()
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) createNotificationChanel() else startForeground(
                1,
                Notification()
        )
        socket.createSocket()
        requestLocationUpdates()
    }

    private fun requestLocationUpdates() {
        val request = LocationRequest()
        request.interval = 120000
        request.fastestInterval = 100000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this)

        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION

        )
        if (permission == PackageManager.PERMISSION_GRANTED) { // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location: Location = locationResult.lastLocation
                    latitude = location.latitude
                    longitude = location.longitude
//                    Toast.makeText(applicationContext, "Update $location", Toast.LENGTH_SHORT).show()
                    socket.sendSocket("rider_location", latitude, longitude, repoPrefs.getLoggedInUser()?.sf_id.toString())
                }
            }, null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChanel() {
        val NOTIFICATION_CHANNEL_ID = "com.tubianto"
        val channelName = "Background Service"
        val chan = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getLocationTitle())
                .setOngoing(true)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.cashtek_trans)
        startForeground(2, builder.build())
    }

    fun getLocationTitle(): String? {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar[Calendar.HOUR_OF_DAY]
        return if (hourOfDay <= 11) {
            "Good Morning"
        } else if (hourOfDay <= 17) {
            "Good Afternoon"
        } else if (hourOfDay <= 19) {
            "Good Evening"
        } else {
            "Good Night"
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        /*RESTART SERVICE WHERE APPS KILL*/
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, RestartBackgroundService::class.java)
        this.sendBroadcast(broadcastIntent)
        /*RESTART SERVICE WHERE APPS KILL*/
    }
}