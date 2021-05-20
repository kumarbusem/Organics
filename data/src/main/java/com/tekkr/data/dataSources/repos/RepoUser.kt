package com.tekkr.data.dataSources.repos

import com.tekkr.data.dataSources.definitions.DataSourceUser
import com.tekkr.data.internal.dataSourceImpls.DataSourceImplUser
import com.tekkr.data.models.User

class RepoUser : DataSourceUser() {

    private val mUserDataSource: DataSourceUser by lazy { DataSourceImplUser() }

    override suspend fun loginRider(phoneNumber: String, password: String, res: (User?) -> Unit) = mUserDataSource.loginRider(phoneNumber, password, res)
    override suspend fun loginFps(phoneNumber: String, password: String, res: (User?) -> Unit) = mUserDataSource.loginFps(phoneNumber, password, res)

}