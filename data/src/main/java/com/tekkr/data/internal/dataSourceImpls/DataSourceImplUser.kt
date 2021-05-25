package com.tekkr.data.internal.dataSourceImpls

import android.util.Log
import com.tekkr.data.dataSources.definitions.DataSourceUser
import com.tekkr.data.models.SimpleResponse
import com.tekkr.data.models.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

internal class DataSourceImplUser : DataSourceUser() {


    override suspend fun sendOTP(phone: String, res: (SimpleResponse) -> Unit) {
        res(apiRequest { API.sendOtp(phone) })
    }

    override suspend fun verifyOTP(phone: String, otp: String, res: (User?) -> Unit) {
        res(apiRequest { API.verifyOtp(phone, otp) })
    }
}