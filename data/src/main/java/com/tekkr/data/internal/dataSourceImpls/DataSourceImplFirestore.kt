package com.tekkr.data.internal.dataSourceImpls

import android.util.Log
import com.tekkr.data.dataSources.definitions.DataSourceFirestore
import com.tekkr.data.models.Settings

internal class DataSourceImplFirestore : DataSourceFirestore() {


    override fun getAppSettings(res: (Settings?) -> Unit) {

        SETTINGS.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("SETTINGS error:", error.toString())
                res(null)
            }
            if (snapshot != null && snapshot.exists()) res(snapshot.toObject(Settings::class.java))
            else res(null)
        }
    }


}