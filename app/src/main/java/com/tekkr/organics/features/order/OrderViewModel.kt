package com.tekkr.organics.features.order

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tekkr.data.internal.common.ApiException
import com.tekkr.data.internal.common.RiderLoginException
import com.tekkr.data.models.CartData
import com.tekkr.data.models.Customer
import com.tekkr.data.models.Order
import com.tekkr.data.models.OrderItem
import com.tekkr.data.roomDatabase.Address
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.data.roomDatabase.toJsonObject
import com.tekkr.organics.common.BaseViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


class OrderViewModel(context: Application) : BaseViewModel(context) {

    val obsCartCount: MutableLiveData<Int> = MutableLiveData()
    val obsCartPrice: MutableLiveData<Int> = MutableLiveData()
    val obsDeliveryAddress: MutableLiveData<Address> = MutableLiveData()
    val obsCustomer: MutableLiveData<Customer> = MutableLiveData()
    val obsItemsList: MutableLiveData<List<BigItem>> = MutableLiveData()

    init {
        getOrder()
    }


    fun getOrder() {

        val order = repoPrefs.getSelectedOrder()
        order?.order_items?.getCounts() { count, price ->
            obsCartCount.postValue(count)
            obsCartPrice.postValue(price)
        }
        val items = order?.order_items!!
                .sortedBy { it.item_details.priority }
                .map {

                    it.item_details
                }

        obsItemsList.postValue(items)
        obsCustomer.postValue(order.customer)
        obsDeliveryAddress.postValue(order.delivery_address)
    }


    public fun List<OrderItem>.getCounts(res: (Int, Int) -> Unit) {
        var cartCount = 0
        var cartPrice = 0

        this.forEach {
            cartCount += it.quantity
            cartPrice += (it.quantity * it.item_details.item_price)
        }
        res(cartCount, cartPrice)
    }

}