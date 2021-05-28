package com.tekkr.organics.features.newAddress

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tekkr.data.roomDatabase.Address
import com.tekkr.organics.common.BaseViewModel
import kotlinx.coroutines.launch

class NewAddressViewModel(context: Application) : BaseViewModel(context) {

    val obsSavedAddressList: MutableLiveData<List<Address>> = MutableLiveData()
    val obsTempAddress: MutableLiveData<Address> = MutableLiveData()
    init {
        getAddress()
    }

    fun getAddress() {
        obsTempAddress.postValue(repoPrefs.getTempAddress())
    }

    fun setSavedAddress(address: Address?){
        ioScope.launch {
            roomRepository.insertAddress(address!!)
        }
    }

}