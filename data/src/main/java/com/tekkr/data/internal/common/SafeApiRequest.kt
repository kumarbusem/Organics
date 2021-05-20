package com.tekkr.data.internal.common

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        Log.e("Safe API Request::", response.toString())
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            if (response.code() == 401) {
                throw RiderLoginException()
            }
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                }
                try {
                    message.append(JSONObject(it).getString("error"))
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            throw ApiException(message.toString())
        }
    }


}