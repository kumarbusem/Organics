package com.tekkr.data.roomDatabase

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.android.gms.common.util.CrashUtils
import com.tekkr.data.dataSources.definitions.DataSourceBasic
import com.tekkr.data.dataSources.repos.RepoBasic
import com.tekkr.data.internal.common.ApiException

class TekkrRoomRepository(private val itemsDao: ItemDao, private val addressDao: AddressDao) {

    val repoBasic: DataSourceBasic by lazy { RepoBasic() }

    suspend fun getAllItems(): List<BigItem> {

        lateinit var onlineItems: List<Item>
        var message: String = ""

        try {
            repoBasic.getItems(){
                onlineItems = it!!
                Log.e("Room Repo Online::", it.toString())
            }
        }catch (e: Exception){
            message = e.message.toString()
        }

        if(onlineItems.isNullOrEmpty()){

            val offlineItems = itemsDao.getAllItems()

            if(offlineItems.isNullOrEmpty()){
                throw ApiException("Items Not Available\n$message")
            }
            else{
                return offlineItems
            }
        }else{
            try {
                itemsDao.makeOfflineItemsInactive()
            }catch (e: java.lang.Exception){

            }
            onlineItems.forEach {item ->
                val items: List<BigItem?>? = itemsDao.getItemById(item.id)
                if (items.isNullOrEmpty())
                    itemsDao.insert(item.toBigItem())
                else
                    itemsDao.update(item)
            }
            return itemsDao.getAllItems()
        }

        return itemsDao.getAllItems()
    }

    suspend fun getCartItems(): List<BigItem>{
        return itemsDao.getCartItems()
    }

    suspend fun clearCartItems(){
         itemsDao.clearCartItems()
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAddresses(): List<Address>{
        return addressDao.getAddresses()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAddress(address: Address) {
        addressDao.insert(address)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateCartItem(item: CartItem) {
        Log.e("UPDATE::", "${item.id}, ${item.number}")
        itemsDao.update(item)
    }

}
