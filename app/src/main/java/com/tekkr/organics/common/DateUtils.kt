package com.tekkr.organics.common

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val TAG: String = "DateUtils"

    const val DB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"


    private const val DISPLAY_DATE_FORMAT: String = "dd MMM YY"
    private const val DISPLAY_SHORT_DATE_FORMAT: String = "dd MMMM"
    private const val DISPLAY_TIME_FORMAT: String = "hh:mm aa dd MMM YY"
    private const val DISPLAY_DATE_TIME_FORMAT: String = "dd MMM YY, hh:mm aa"
    private const val DISPLAY_DATE_TIME_REPORT_TILE_FORMAT: String = "hh mm aa, dd MMM YY"

    fun String.getDate(): Date? {
        return try {
            val fromFormat = SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault())
            fromFormat.timeZone = TimeZone.getTimeZone("UTC")
            fromFormat.parse(this)
        } catch (ex: ParseException) {
            Log.e(TAG, "Failed to parse $this. ${ex.message}.")
            null
        }
    }

    fun getDateToDisplay(timeStamp: String?): String? {
        if (timeStamp.isNullOrBlank()) return null
        return timeStamp.formatWith(DISPLAY_DATE_FORMAT)
    }

    fun getTimeToDisplay(timeStamp: String?): String? {
        if (timeStamp.isNullOrBlank()) return null
        return timeStamp.formatWith(DISPLAY_TIME_FORMAT)
    }

    fun getDateTimeToDisplay(timeStamp: String?): String? {
        if (timeStamp.isNullOrBlank()) return null
        return timeStamp.formatWith(DISPLAY_DATE_TIME_FORMAT)
    }

    fun getReportTitleDateToDisplay(timeStamp: String?): String? {
        if (timeStamp.isNullOrBlank()) return null
        return timeStamp.formatWith(DISPLAY_DATE_TIME_REPORT_TILE_FORMAT)
    }

    fun getShortDateToDisplay(timeStamp: String?): String? {
        if (timeStamp.isNullOrBlank()) return null
        return timeStamp.formatWith(DISPLAY_SHORT_DATE_FORMAT)
    }

    private fun String.formatWith(format: String): String? {

        val timeStamp = this

        return try {
            Log.e("TIME: ", timeStamp.toString())
            val fromFormat = SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault())
            fromFormat.timeZone = TimeZone.getTimeZone("UTC")
            fromFormat.parse(timeStamp)?.let { fromDate ->

                val toFormat = SimpleDateFormat(format, Locale.getDefault())
                toFormat.timeZone = TimeZone.getDefault()
                return toFormat.format(fromDate)
            } ?: return null

        } catch (e: Exception) {
            Log.e(TAG, "${e.message}.")
            null
        }
    }
}