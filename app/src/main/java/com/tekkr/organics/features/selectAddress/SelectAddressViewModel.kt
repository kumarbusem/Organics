package com.tekkr.organics.features.selectAddress

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.model.Place
import com.tekkr.data.roomDatabase.Address
import com.tekkr.organics.common.BaseViewModel
import kotlinx.coroutines.launch

class SelectAddressViewModel(context: Application) : BaseViewModel(context) {

    val obsSavedAddressList: MutableLiveData<List<Address>> = MutableLiveData()
    init {
        getSavedAddress()
    }

    fun getSavedAddress() {

        ioScope.launch {
            try {
                val allRecentAddresses: List<Address> = roomRepository.getAddresses()
                allRecentAddresses.forEach {
                    Log.e("WORDS::", it.address.toString())
                }
                obsSavedAddressList.postValue(allRecentAddresses)
            } catch (e: Exception) {
                obsMessage.postValue(e.message + "")
                obsSavedAddressList.postValue(null)
                e.printStackTrace()
            }
        }
    }

    fun setSavedAddress(place: Place){
        ioScope.launch {
            roomRepository.insertAddress(Address(name = place.name.toString(), address = place.address.toString(),
                    latitude = place.latLng?.latitude!!, longitude = place.latLng?.longitude!!))
        }
    }

}