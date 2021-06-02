package com.tekkr.data.internal.dataSourceImpls

import android.util.Log
import com.tekkr.data.dataSources.definitions.DataSourceImage
import com.tekkr.data.internal.common.ApiException
import com.tekkr.data.models.SimpleResponse
import com.tekkr.data.models.User
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call

internal class DataSourceImplImage : DataSourceImage() {



    private fun <T : Any> baseImageUpload(call: Call<T>): T? {

        val response = call.execute()
        Log.e("Base Image Response::", response.toString())
        if (response.isSuccessful)
            return response.body()
        else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            throw ApiException(message.toString())
        }
    }

}