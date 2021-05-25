package com.tekkr.data.internal.dataSourceImpls

import com.tekkr.data.dataSources.definitions.DataSourceBasic
import com.tekkr.data.models.Order
import com.tekkr.data.models.OrderResponse
import com.tekkr.data.roomDatabase.Item
import com.tekkr.data.models.SimpleResponse
import okhttp3.RequestBody

internal class DataSourceImplBasic : DataSourceBasic() {


    override suspend fun getItems(res: (List<Item>?) -> Unit) {
        res(apiRequest { API.getItems() })
    }

}