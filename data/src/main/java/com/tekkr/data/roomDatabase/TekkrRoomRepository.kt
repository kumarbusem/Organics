package com.tekkr.data.roomDatabase

import android.util.Log
import androidx.annotation.WorkerThread
import com.tekkr.data.dataSources.definitions.DataSourceBasic
import com.tekkr.data.dataSources.repos.RepoBasic
import com.tekkr.data.internal.common.ApiException

class TekkrRoomRepository(private val itemsDao: ItemDao) {

    val repoBasic: DataSourceBasic by lazy { RepoBasic() }

    suspend fun getAllItems(): List<BigItem> {

        lateinit var onlineItems: List<Item>
        var message: String = ""

        try {
            repoBasic.getItems(){ onlineItems = it!! }
        }catch (e: Exception){
            message = e.message.toString()
        }
        val offlineItems = itemsDao.getAllItems()

        if(onlineItems.isNullOrEmpty()){
            if(offlineItems.isNullOrEmpty()){
                throw ApiException("Items Not Available\n$message")
            }
            else{
                return offlineItems
            }
        }else{

            onlineItems.forEach {item ->

                val items: List<BigItem?>? = itemsDao.getItemById(item.id)
                if (items.isNullOrEmpty())
                    itemsDao.insert(item.toBigItem())
                else
                    itemsDao.update(item)
            }
            return offlineItems
        }

        return itemsDao.getAllItems()
    }

    suspend fun getCartItems(): List<BigItem>{

        return itemsDao.getCartItems()

    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(item: BigItem) {
        itemsDao.insert(item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(item: Item) {
        val items: List<BigItem?>? = itemsDao.getItemById(item.id)
        if (items.isNullOrEmpty())
            itemsDao.insert(item.toBigItem())
        else
            itemsDao.update(item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(item: CartItem) {
        Log.e("UPDATE::", "${item.id}, ${item.number}")
        itemsDao.update(item)
    }

}
