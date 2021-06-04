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
import com.tekkr.data.models.*
import com.tekkr.data.roomDatabase.Address
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.data.roomDatabase.toJsonObject
import com.tekkr.organics.common.BaseViewModel
import com.tekkr.organics.common.isStatusSuccess
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


class OrderViewModel(context: Application) : BaseViewModel(context) {

    val obsOrder: MutableLiveData<Order> = MutableLiveData()
    val obsCartCount: MutableLiveData<Int> = MutableLiveData()
    val obsCartPrice: MutableLiveData<Int> = MutableLiveData()
    val obsDeliveryAddress: MutableLiveData<Address> = MutableLiveData()
    val obsItemsList: MutableLiveData<List<BigItem>> = MutableLiveData()

    init {
        getOrder()
    }


    fun getOrder() {

        val order = repoPrefs.getSelectedOrder()
        obsOrder.postValue(order)
        order?.order_items?.getCounts() { count, price ->
            obsCartCount.postValue(count)
            obsCartPrice.postValue(price)
        }
        val items = order?.order_items!!
                .sortedBy { it.item_details.priority }
                .map {
                    it.getOrderItem()
                }

        obsItemsList.postValue(items)
        obsDeliveryAddress.postValue(order.delivery_address)

        if(!order.payment_verified) verifyPaymentWithOrder()

    }

    fun OrderItem.getOrderItem(): BigItem {
        val item = this.item_details
        item.number = this.quantity
        return item
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

    fun verifyPaymentWithOrder() {
        ioScope.launch {
            try {
                repoBasic.verifyPaymentWithOrder {

                    if(it != null && it.status.isStatusSuccess()){

                        val order = repoPrefs.getSelectedOrder()
                        order?.payment_verified = true
                        obsOrder.postValue(order)

                    }

                }
            } catch (e: Exception) {

            }
        }
    }

}