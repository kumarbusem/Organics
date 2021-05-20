package com.tekkr.data.dataSources.definitions

import com.tekkr.data.models.ProfilePicResponse
import com.tekkr.data.models.SimpleResponse
import okhttp3.RequestBody

abstract class DataSourceImage : BaseDataSource() {


    abstract suspend fun updateOrder(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)

    abstract suspend fun uploadProfilePic(requestBody: RequestBody, res: (ProfilePicResponse?) -> Unit)
}