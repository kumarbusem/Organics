package com.tekkr.data.dataSources.repos

import com.tekkr.data.dataSources.definitions.DataSourceImage
import com.tekkr.data.internal.dataSourceImpls.DataSourceImplImage
import com.tekkr.data.models.ProfilePicResponse
import com.tekkr.data.models.SimpleResponse
import okhttp3.RequestBody

class RepoImage : DataSourceImage() {

    private val mImageDataSource: DataSourceImage by lazy { DataSourceImplImage() }

    override suspend fun uploadProfilePic(requestBody: RequestBody, res: (ProfilePicResponse?) -> Unit) = mImageDataSource.uploadProfilePic(requestBody, res)
    override suspend fun updateOrder(requestBody: RequestBody, res: (SimpleResponse?) -> Unit) = mImageDataSource.updateOrder(requestBody, res)

}