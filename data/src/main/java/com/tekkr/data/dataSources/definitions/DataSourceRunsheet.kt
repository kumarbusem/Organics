package com.tekkr.data.dataSources.definitions

import com.tekkr.data.models.Order
import com.tekkr.data.models.OrderResponse
import com.tekkr.data.models.Ration
import com.tekkr.data.models.SimpleResponse
import okhttp3.RequestBody

abstract class DataSourceRunsheet : BaseDataSource() {

    abstract suspend fun getOrders(res: (List<Order>?) -> Unit)
    abstract suspend fun getFpsOrders(res: (List<Order>?) -> Unit)
    abstract suspend fun getRations(res: (List<Ration>?) -> Unit)

    abstract suspend fun getOrder(rationId: String, res: (OrderResponse?) -> Unit)

    abstract suspend fun changePassword(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)
}