package com.tekkr.organics.features.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tekkr.data.internal.common.ApiException
import com.tekkr.data.internal.common.RiderLoginException
import com.tekkr.data.roomDatabase.Item
import com.tekkr.organics.common.BaseViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class HomeViewModel(context: Application) : BaseViewModel(context) {

    val obsItemsList: MutableLiveData<List<Item>> = MutableLiveData()
    val obsFruitsList: MutableLiveData<List<Item>> = MutableLiveData()
    val obsVegetablesList: MutableLiveData<List<Item>> = MutableLiveData()
    val obsMeatList: MutableLiveData<List<Item>> = MutableLiveData()

    init {
        getItems()
    }

    fun getItems() {
        obsIsDataLoading.postValue(true)
        ioScope.launch {
            try {
                repoBasic.getItems { items ->

                    var fruits: ArrayList<Item> = ArrayList()
                    var vegetables: ArrayList<Item> = ArrayList()
                    var meat: ArrayList<Item> = ArrayList()

                    items?.forEach {
                        when (it.category) {
                            1 -> fruits.add(it)
                            2 -> vegetables.add(it)
                            3 -> {
                                meat.add(it)
                                ioScope.launch { roomRepository.insert(it) }
                            }
                        }
                    }

                    Log.e("Room Items::", roomRepository.allRecentAddresses.toString())

                    obsItemsList.postValue(items)
                    obsFruitsList.postValue(fruits)
                    obsVegetablesList.postValue(vegetables)
                    obsMeatList.postValue(meat)
                    obsIsDataLoading.postValue(false)
                }
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
}