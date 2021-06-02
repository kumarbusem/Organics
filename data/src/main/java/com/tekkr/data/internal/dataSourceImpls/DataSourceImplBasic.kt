package com.tekkr.data.internal.dataSourceImpls

import android.util.Log
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.tekkr.data.dataSources.definitions.DataSourceBasic
import com.tekkr.data.models.*
import com.tekkr.data.roomDatabase.Item
import okhttp3.RequestBody

internal class DataSourceImplBasic : DataSourceBasic() {

    override suspend fun getItems(res: (List<Item>?) -> Unit) {
        res(apiRequest { API.getItems() })
    }

    override suspend fun placeOrder(res: (Order) -> Unit) {
        Log.e("ORDER BODY::", repoPrefs.getOrderBody().toString())
        res(apiRequest { API.placeOrder(repoPrefs.getOrderBody()!!, " Bearer " + repoPrefs.getLoggedInUser()?.access!!) })
    }

    override suspend fun getOrders(res: (List<Order>) -> Unit) {
        res(apiRequest { API.getOrders(" Bearer " + repoPrefs.getLoggedInUser()?.access!!) })
    }

}