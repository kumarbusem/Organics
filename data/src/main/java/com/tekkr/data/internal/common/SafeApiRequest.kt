package com.tekkr.data.internal.common

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
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
            }else if(response.code() == 400){
                throw ApiException(error.toString())
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
            message.append("${response.code()}")
            throw ApiException(message.toString())
        }
    }



}