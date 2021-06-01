package com.tekkr.organics.features.payment

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tekkr.data.internal.common.ApiException
import com.tekkr.data.internal.common.RiderLoginException
import com.tekkr.data.models.SimpleResponse
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.data.roomDatabase.Item
import com.tekkr.organics.common.BaseViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class PaymentViewModel(context: Application) : BaseViewModel(context) {

    val obsItemsList: MutableLiveData<List<BigItem>> = MutableLiveData()

    init {
        getUser()
    }

    fun placeOrder() {
        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {

                repoBasic.placeOrder {
                    Log.e("PLACE ORDER:::", it.toString())
                }

                obsIsDataLoading.postValue(false)

            } catch (e: ApiException) {
                Log.e("PLACE ORDER:::", "1")
                obsMessage.postValue(e.message!!)
                obsItemsList.postValue(null)
                obsIsDataLoading.postValue(false)
            } catch (e: SocketTimeoutException) {
                Log.e("PLACE ORDER:::", "2")
                obsMessage.postValue("Slow Network!\nPlease ty again")
                obsItemsList.postValue(null)
                obsIsDataLoading.postValue(false)
            } catch (e: RiderLoginException) {
                Log.e("PLACE ORDER:::", "3")
                repoPrefs.clearLoggedInUser()
                isUserLogout.postValue(true)
                obsItemsList.postValue(null)
                obsIsDataLoading.postValue(false)
            } catch (e: Exception) {
                Log.e("PLACE ORDER:::", "4")
                obsIsDataLoading.postValue(false)
                obsItemsList.postValue(null)
                e.printStackTrace()
                if (e.message.toString().contains("Unable to resolve")) obsMessage.postValue("Network Issue\nUnable to resolve host")
                else obsMessage.postValue(e.message)
            }
        }

    }




}