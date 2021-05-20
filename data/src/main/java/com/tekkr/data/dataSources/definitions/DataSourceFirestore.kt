package com.tekkr.data.dataSources.definitions

import com.tekkr.data.models.Settings

abstract class DataSourceFirestore : BaseDataSource() {

    abstract fun getAppSettings(res: (Settings?) -> Unit)

}