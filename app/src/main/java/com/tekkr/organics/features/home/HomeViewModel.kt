package com.tekkr.organics.features.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tekkr.data.internal.common.ApiException
import com.tekkr.data.internal.common.RiderLoginException
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.data.roomDatabase.Item
import com.tekkr.organics.common.BaseViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class HomeViewModel(context: Application) : BaseViewModel(context) {

    val obsItemsList: MutableLiveData<List<BigItem>> = MutableLiveData()
    val obsFruitsList: MutableLiveData<List<BigItem>> = MutableLiveData()
    val obsVegetablesList: MutableLiveData<List<BigItem>> = MutableLiveData()
    val obsMeatList: MutableLiveData<List<BigItem>> = MutableLiveData()

    init {
        getItems()
    }

    fun getItems() {
        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {
                val items = roomRepository.getAllItems()


                var fruits: ArrayList<BigItem> = ArrayList()
                var vegetables: ArrayList<BigItem> = ArrayList()
                var meat: ArrayList<BigItem> = ArrayList()

                items?.forEach {
                    when (it.category) {
                        1 -> fruits.add(it)
                        2 -> vegetables.add(it)
                        3 -> meat.add(it)
                    }
                }

                Log.e("Room Items::", roomRepository.getAllItems().toString())

                obsItemsList.postValue(items)
                obsFruitsList.postValue(fruits)
                obsVegetablesList.postValue(vegetables)
                obsMeatList.postValue(meat)
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

    fun updateItem(id: Int, number: Int) {

        ioScope.launch {
            roomRepository.update(CartItem(id, number))
        }

    }
}