package com.tekkr.data.internal.dataSourceImpls

import android.util.Log
import com.tekkr.data.dataSources.definitions.DataSourceBasic
import com.tekkr.data.internal.common.RiderLoginException
import com.tekkr.data.internal.common.baseUrl
import com.tekkr.data.models.*
import com.tekkr.data.roomDatabase.Item
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

internal class DataSourceImplBasic : DataSourceBasic() {

    override suspend fun getItems(res: (List<Item>?) -> Unit) {
        res(apiRequest { API.getItems() })
    }

    override suspend fun placeOrder(res: (Order) -> Unit) {
        retryOnTokenExpiry {
            res(apiRequest { API.placeOrder(repoPrefs.getOrderBody()!!, " Bearer " + repoPrefs.getLoggedInUser()?.access!!) })
        }
    }

    override suspend fun getOrders(res: (List<Order>) -> Unit) {
        retryOnTokenExpiry {
            res(apiRequest { API.getOrders(" Bearer " + repoPrefs.getLoggedInUser()?.access!!) })
        }
    }

    override suspend fun verifyPayment(requestBody: RequestBody, res: (SimpleResponse?) -> Unit) {
        retryOnTokenExpiry {
            res(apiRequest { API.verifyPayment(requestBody, " Bearer " + repoPrefs.getLoggedInUser()?.access!!) })
        }
    }

    override suspend fun verifyPaymentWithOrder(res: (SimpleResponse?) -> Unit) {
        retryOnTokenExpiry {
            res(apiRequest { API.verifyPaymentWithOrder(repoPrefs.getSelectedOrder()?.id.toString(), " Bearer " + repoPrefs.getLoggedInUser()?.access!!) })
        }
    }

    private suspend fun retryOnTokenExpiry(logic: suspend () -> Unit) {
        try {
            logic()
        }catch (ex : RiderLoginException){ // Change this with session exception
            refreshToken()
            logic()
        }
    }

    private suspend fun refreshToken() {
        val user = repoPrefs.getLoggedInUser()
        if(user != null && user.refresh.isNotEmpty()){
            val json = JSONObject()
            json.put("refresh", user.refresh)
            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
            val accress = apiRequest { API.refreshToken(requestBody) }
            user.access = accress.access
            repoPrefs.saveLoggedInUser(user)
        }else{
            throw RiderLoginException()
        }

    }

}