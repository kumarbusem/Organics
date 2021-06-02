package com.tekkr.organics.features.cart

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
import com.tekkr.data.roomDatabase.Address
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.data.roomDatabase.toJsonObject
import com.tekkr.organics.common.BaseViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


class CartViewModel(context: Application) : BaseViewModel(context) {

    val obsCartCount: MutableLiveData<Int> = MutableLiveData()
    val obsCartPrice: MutableLiveData<Int> = MutableLiveData()
    val obsDeliveryAddress: MutableLiveData<Address> = MutableLiveData()
    val obsCustomer: MutableLiveData<Customer> = MutableLiveData()
    val obsItemsList: MutableLiveData<List<BigItem>> = MutableLiveData()

    init {
        getItems()
        getUser()
        getContactDetails()
    }


    fun getItems() {
        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {
                val items = roomRepository.getCartItems()

                items.getCounts() { count, price ->
                    obsCartCount.postValue(count)
                    obsCartPrice.postValue(price)
                }

                obsItemsList.postValue(items)
                obsIsDataLoading.postValue(false)

            } catch (e: ApiException) {
                obsMessage.postValue(e.message!!)
                obsItemsList.postValue(null)
                obsIsDataLoading.postValue(false)
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network!\nPlease ty again")
                obsItemsList.postValue(null)
                obsIsDataLoading.postValue(false)
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                isUserLogout.postValue(true)
                obsItemsList.postValue(null)
                obsIsDataLoading.postValue(false)
            } catch (e: Exception) {
                obsIsDataLoading.postValue(false)
                obsItemsList.postValue(null)
                e.printStackTrace()
                if (e.message.toString().contains("Unable to resolve")) obsMessage.postValue("Network Issue\nUnable to resolve host")
                else obsMessage.postValue(e.message)
            }
        }

    }


    fun updateItemNumber(cartItem: CartItem, itemPrice: Int, type: Boolean) {
        ioScope.launch {
            roomRepository.updateCartItem(cartItem)
            var count: Int = obsCartCount.value!!
            var cartPrice: Int = obsCartPrice.value!!
            if (type) {
                count++
                cartPrice += itemPrice
                obsCartCount.postValue(count)
                obsCartPrice.postValue(cartPrice)
            } else {
                count--
                cartPrice -= itemPrice
                obsCartCount.postValue(count)
                obsCartPrice.postValue(cartPrice)
            }
        }
    }

    fun getDeliveryAddress() {
        obsDeliveryAddress.postValue(repoPrefs.getAddress())
        Log.e("DELIVERY ADDRESSL::", repoPrefs.getAddress().toString())
    }

    fun List<BigItem>.getCounts(res: (Int, Int) -> Unit) {
        var cartCount = 0
        var cartPrice = 0

        this.forEach {
            cartCount += it.number
            cartPrice += (it.number * it.item_price)
        }
        res(cartCount, cartPrice)
    }

    fun getContactDetails() {
        obsCustomer.postValue(repoPrefs.getContactDetails())
    }
//    fun generateOrderBody2(res: () -> Unit) {
//
//        val address = repoPrefs.getAddress()
//        val contact = repoPrefs.getContactDetails()
//        address?.name = contact?.name!!
//        address?.phone_number = contact.phone
//
//        val cartData = obsItemsList.value!!
//                .sortedBy { it.priority }
//                .map { CartData(item = it.id, quantity = it.number) }
//
//        val orderBody = OrderBody(
//                is_new_address = true,
//                delivery_address = address!!,
//                customer_id = repoPrefs.getLoggedInUser()?.id!!,
//                order_items = cartData
//        )
//
//        repoPrefs.saveOrderBody(orderBody)
//        res()
//    }
    fun generateOrderBody(res: () -> Unit) {

        val address = repoPrefs.getAddress()
        val contact = repoPrefs.getContactDetails()
        address?.name = contact?.name!!
        address?.phone_number = contact.phone_number

        val cartData = obsItemsList.value!!
                .sortedBy { it.priority }
                .map { CartData(item = it.id, quantity = it.number) }

        val orderBody = JsonObject()
        orderBody.addProperty("is_new_address", true)
        orderBody.add("delivery_address", address!!.toJsonObject().asJsonObject)
        orderBody.addProperty("customer_id", repoPrefs.getLoggedInUser()?.id)

        val element: JsonElement = Gson().toJsonTree(cartData, object : TypeToken<List<CartData?>?>() {}.type)
        orderBody.add("order_items", element.asJsonArray)

        repoPrefs.saveOrderBody(orderBody)
        res()
    }
}