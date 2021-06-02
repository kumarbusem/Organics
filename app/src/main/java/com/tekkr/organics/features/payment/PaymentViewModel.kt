package com.tekkr.organics.features.payment

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tekkr.data.internal.common.ApiException
import com.tekkr.data.internal.common.RiderLoginException
import com.tekkr.data.models.Order
import com.tekkr.organics.common.BaseViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class PaymentViewModel(context: Application) : BaseViewModel(context) {

    val obsPlaceOrderResponse: MutableLiveData<Order> = MutableLiveData()

    init {
        getUser()
    }

    fun placeOrder() {
        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {
                repoBasic.placeOrder {
                    Log.e("PLACE ORDER:::", it.toString())
                    obsPlaceOrderResponse.postValue(it)
                }
                obsIsDataLoading.postValue(false)

            } catch (e: ApiException) {
                obsMessage.postValue(e.message!!)
                obsIsDataLoading.postValue(false)
            } catch (e: SocketTimeoutException) {
                obsMessage.postValue("Slow Network!\nPlease ty again")
                obsIsDataLoading.postValue(false)
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                isUserLogout.postValue(true)
                obsIsDataLoading.postValue(false)
            } catch (e: Exception) {
                obsIsDataLoading.postValue(false)
                e.printStackTrace()
                if (e.message.toString().contains("Unable to resolve")) obsMessage.postValue("Network Issue\nUnable to resolve host")
                else obsMessage.postValue(e.message)
            }
        }
    }

    fun clearCartItems() {
        ioScope.launch {
            roomRepository.clearCartItems()
        }
    }
}