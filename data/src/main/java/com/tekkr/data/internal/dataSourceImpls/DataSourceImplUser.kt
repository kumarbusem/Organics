package com.tekkr.data.internal.dataSourceImpls

import android.util.Log
import com.tekkr.data.dataSources.definitions.DataSourceUser
import com.tekkr.data.models.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

internal class DataSourceImplUser : DataSourceUser() {


    override suspend fun loginRider(phoneNumber: String, password: String, res: (User?) -> Unit) {
        Log.e("USER IMPL::", "Enter")
        val json = JSONObject()
        json.put("username", phoneNumber)
        json.put("password", password)
        val requestBody: RequestBody =
                RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
        res(apiRequest { API.riderLogin(requestBody) })

    }

    override suspend fun loginFps(phoneNumber: String, password: String, res: (User?) -> Unit) {
        Log.e("USER IMPL::", "Enter")
        val json = JSONObject()
        json.put("username", phoneNumber)
        json.put("password", password)
        val requestBody: RequestBody =
                RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
        res(apiRequest { API.fpsLogin(requestBody) })

    }

}