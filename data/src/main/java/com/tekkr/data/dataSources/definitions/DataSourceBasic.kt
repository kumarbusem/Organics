package com.tekkr.data.dataSources.definitions

import com.tekkr.data.models.Order
import com.tekkr.data.models.OrderResponse
import com.tekkr.data.roomDatabase.Item
import com.tekkr.data.models.SimpleResponse
import okhttp3.RequestBody

abstract class DataSourceBasic : BaseDataSource() {

    abstract suspend fun getItems(res: (List<Item>?) -> Unit)

    abstract suspend fun placeOrder(res: (String) -> Unit)
}