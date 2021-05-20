package com.tekkr.data.dataSources.repos

import com.tekkr.data.dataSources.definitions.DataSourceRunsheet
import com.tekkr.data.internal.dataSourceImpls.DataSourceImplRunsheet
import com.tekkr.data.models.Order
import com.tekkr.data.models.OrderResponse
import com.tekkr.data.models.Ration
import com.tekkr.data.models.SimpleResponse
import okhttp3.RequestBody

class RepoRunsheet : DataSourceRunsheet() {

    private val mRunsheetDataSource: DataSourceRunsheet by lazy { DataSourceImplRunsheet() }

    override suspend fun getOrders(res: (List<Order>?) -> Unit) = mRunsheetDataSource.getOrders(res)
    override suspend fun getFpsOrders(res: (List<Order>?) -> Unit) = mRunsheetDataSource.getFpsOrders(res)
    override suspend fun getRations(res: (List<Ration>?) -> Unit) = mRunsheetDataSource.getRations(res)
    override suspend fun getOrder(rationId: String, res: (OrderResponse?) -> Unit) = mRunsheetDataSource.getOrder(rationId, res)

    override suspend fun changePassword(requestBody: RequestBody, res: (SimpleResponse?) -> Unit) = mRunsheetDataSource.changePassword(requestBody, res)
}