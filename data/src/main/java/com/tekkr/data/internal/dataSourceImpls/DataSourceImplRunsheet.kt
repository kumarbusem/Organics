package com.tekkr.data.internal.dataSourceImpls

import com.tekkr.data.dataSources.definitions.DataSourceRunsheet
import com.tekkr.data.models.Order
import com.tekkr.data.models.OrderResponse
import com.tekkr.data.models.Ration
import com.tekkr.data.models.SimpleResponse
import okhttp3.RequestBody

internal class DataSourceImplRunsheet : DataSourceRunsheet() {


    override suspend fun getOrders(res: (List<Order>?) -> Unit) {
        res(apiRequest { API.getOrders(repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun getFpsOrders(res: (List<Order>?) -> Unit) {
        res(apiRequest { API.getFpsOrders(repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun getRations(res: (List<Ration>?) -> Unit) {
        res(apiRequest { API.getRations(repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun changePassword(requestBody: RequestBody, res: (SimpleResponse?) -> Unit) {
        res(apiRequest { API.changePassword(requestBody, repoPrefs.getLoggedInUser()?.token) })
    }

    override suspend fun getOrder(rationId: String, res: (OrderResponse?) -> Unit) {
        res(apiRequest { API.getOrder(rationId, repoPrefs.getLoggedInUser()?.token) })
    }

}