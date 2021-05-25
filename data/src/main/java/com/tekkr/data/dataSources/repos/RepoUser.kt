package com.tekkr.data.dataSources.repos

import com.tekkr.data.dataSources.definitions.DataSourceUser
import com.tekkr.data.internal.dataSourceImpls.DataSourceImplUser
import com.tekkr.data.models.SimpleResponse
import com.tekkr.data.models.User
import okhttp3.RequestBody

class RepoUser : DataSourceUser() {

    private val mUserDataSource: DataSourceUser by lazy { DataSourceImplUser() }

    override suspend fun sendOTP(phone: String, res: (SimpleResponse) -> Unit)
            = mUserDataSource.sendOTP(phone, res)

    override suspend fun verifyOTP(phone: String, otp: String, res: (User?) -> Unit)
            = mUserDataSource.verifyOTP(phone, otp, res)

}