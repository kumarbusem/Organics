package com.tekkr.organics.features.cart

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tekkr.data.internal.common.ApiException
import com.tekkr.data.internal.common.RiderLoginException
import com.tekkr.data.roomDatabase.Address
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.organics.common.BaseViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CartViewModel(context: Application) : BaseViewModel(context) {

    val obsCartCount: MutableLiveData<Int> = MutableLiveData()
    val obsCartPrice: MutableLiveData<Int> = MutableLiveData()
    val obsDeliveryAddress: MutableLiveData<Address> = MutableLiveData()
    val obsItemsList: MutableLiveData<List<BigItem>> = MutableLiveData()

    init {
        getItems()
        getUser()
    }



    fun getItems() {
        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {
                val items = roomRepository.getCartItems()

                var cartCount = 0
                var cartPrice = 0

                items.forEach {

                    cartCount += it.number
                    cartPrice += (it.number * it.item_price)
                }

                obsCartCount.postValue(cartCount)
                obsCartPrice.postValue(cartPrice)

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
            if(type){
                count ++
                cartPrice += itemPrice
                obsCartCount.postValue(count)
                obsCartPrice.postValue(cartPrice)
            }else{
                count --
                cartPrice -= itemPrice
                obsCartCount.postValue(count)
                obsCartPrice.postValue(cartPrice)
            }
        }
    }

    fun getDeliveryAddress() {
        obsDeliveryAddress.postValue(repoPrefs.getAddress())
    }


}