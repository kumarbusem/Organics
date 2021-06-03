package com.tekkr.data.dataSources.repos

import com.tekkr.data.dataSources.definitions.DataSourceBasic
import com.tekkr.data.internal.dataSourceImpls.DataSourceImplBasic
import com.tekkr.data.models.Order
import com.tekkr.data.models.PaymentVerifyResponse
import com.tekkr.data.roomDatabase.Item
import com.tekkr.data.models.SimpleResponse
import okhttp3.RequestBody

class RepoBasic : DataSourceBasic() {

    private val mRunsheetDataSource: DataSourceBasic by lazy { DataSourceImplBasic() }

    override suspend fun getItems(res: (List<Item>?) -> Unit) = mRunsheetDataSource.getItems(res)

    override suspend fun getOrders(res: (List<Order>) -> Unit) = mRunsheetDataSource.getOrders(res)

    override suspend fun placeOrder(res: (Order) -> Unit) = mRunsheetDataSource.placeOrder(res)

    override suspend fun verifyPayment(requestBody: RequestBody, res: (PaymentVerifyResponse?) -> Unit) = mRunsheetDataSource.verifyPayment(requestBody, res)


}