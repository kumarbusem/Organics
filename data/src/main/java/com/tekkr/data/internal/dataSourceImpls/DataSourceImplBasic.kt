package com.tekkr.data.internal.dataSourceImpls

import android.util.Log
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.tekkr.data.dataSources.definitions.DataSourceBasic
import com.tekkr.data.models.Order
import com.tekkr.data.models.OrderBody
import com.tekkr.data.models.OrderResponse
import com.tekkr.data.roomDatabase.Item
import com.tekkr.data.models.SimpleResponse
import okhttp3.RequestBody

internal class DataSourceImplBasic : DataSourceBasic() {


    override suspend fun getItems(res: (List<Item>?) -> Unit) {
        res(apiRequest { API.getItems() })
    }

    override suspend fun placeOrder(res: (String) -> Unit) {



        Log.e("USER ::", repoPrefs.getLoggedInUser()?.access.toString())

        val element: JsonElement = Gson().toJsonTree(repoPrefs.getOrderBody(), object : TypeToken<OrderBody>() {}.type)
        Log.e("ORDER BODY::", element.asJsonObject.toString())

        val kkk = apiRequest { API.placeOrder(element.asJsonObject, " Bearer " + repoPrefs.getLoggedInUser()?.access!!) }

        res(kkk)

    }

}