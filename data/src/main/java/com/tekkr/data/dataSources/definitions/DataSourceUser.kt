package com.tekkr.data.dataSources.definitions

import com.tekkr.data.models.SimpleResponse
import com.tekkr.data.models.User
import okhttp3.RequestBody

abstract class DataSourceUser : BaseDataSource() {

    abstract suspend fun sendOTP(phone: String, res: (SimpleResponse) -> Unit)

    abstract suspend fun verifyOTP(phone: String, otp: String, res: (User?) -> Unit)

}