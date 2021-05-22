package com.tekkr.data.dataSources.definitions

import com.tekkr.data.models.Order
import com.tekkr.data.models.OrderResponse
import com.tekkr.data.roomDatabase.Item
import com.tekkr.data.models.SimpleResponse
import okhttp3.RequestBody

abstract class DataSourceBasic : BaseDataSource() {

    abstract suspend fun getItems(res: (List<Item>?) -> Unit)

    abstract suspend fun getOrders(res: (List<Order>?) -> Unit)
    abstract suspend fun getFpsOrders(res: (List<Order>?) -> Unit)

    abstract suspend fun getOrder(rationId: String, res: (OrderResponse?) -> Unit)

    abstract suspend fun changePassword(requestBody: RequestBody, res: (SimpleResponse?) -> Unit)
}