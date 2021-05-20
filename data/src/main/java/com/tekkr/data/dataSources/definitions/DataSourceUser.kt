package com.tekkr.data.dataSources.definitions

import com.tekkr.data.models.User

abstract class DataSourceUser : BaseDataSource() {

    abstract suspend fun loginRider(phoneNumber: String, password: String, res: (User?) -> Unit)
    abstract suspend fun loginFps(phoneNumber: String, password: String, res: (User?) -> Unit)

}