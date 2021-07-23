package com.tekkr.organics.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.view.Display
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible


fun getRandomUDID(): String = UUID.randomUUID().toString()

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun String?.getOtp(): String {

    val matcher: Matcher = Pattern.compile("(\\d{4})").matcher(this)
    return if (matcher.find()) matcher.group(0) else ""

}

fun isGpsAvailable(context: Context): Boolean {
    val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun String.searchWordsFilter(filter: String): Boolean {

    var boo = true
    filter.split(' ').forEach {
        if (this.contains(it, true)) boo = true
        else return false
    }
    return boo
}

fun String.isStatusSuccess(): Boolean {
    return this == "success"
}
fun String.isStatusFailed(): Boolean {
    return this == "failed"
}

fun String.isAlreadyPaid(): Boolean {
    return this == "STATUS_ALREADY_PAID"
}

fun getCurrentAppVersion(context: Context): String {
    val manager = context.packageManager
    val info = manager.getPackageInfo(context.packageName, 0)
    return info.versionName
}

fun getCurrentHalfScreen(activity: Activity, d: Double): Int {
    val display: Display = activity.windowManager.defaultDisplay
    val width = display.width
    return (width / d).toInt()
}


val KProperty0<*>.isLazyInitialized: Boolean
    get() {
        // Prevent IllegalAccessException from JVM access check
        isAccessible = true
        return (getDelegate() as Lazy<*>).isInitialized()
    }

fun getCurrentDateInServerFormat(): String = getTimeInServerFormat()

public fun getTimeInServerFormat(): String {

    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return simpleDateFormat.format(Date())
}

@SuppressLint("MissingPermission")
fun getLocation(context: Context, res: (Location?) -> Unit) {
    LocationServices.getFusedLocationProviderClient(context).lastLocation
            .addOnSuccessListener { location: Location? ->
                res(location)
            }.addOnFailureListener {
                res(null)
            }
}