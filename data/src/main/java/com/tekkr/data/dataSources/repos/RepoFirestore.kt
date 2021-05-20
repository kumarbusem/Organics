package com.tekkr.data.dataSources.repos

import com.tekkr.data.dataSources.definitions.DataSourceFirestore
import com.tekkr.data.internal.dataSourceImpls.DataSourceImplFirestore
import com.tekkr.data.models.Settings

class RepoFirestore : DataSourceFirestore() {

    private val mUserDataSource: DataSourceFirestore by lazy { DataSourceImplFirestore() }

    override fun getAppSettings(res: (Settings?) -> Unit) = mUserDataSource.getAppSettings(res)

}