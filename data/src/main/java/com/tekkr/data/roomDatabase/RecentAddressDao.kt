package com.tekkr.data.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecentAddressDao {

    @Query("SELECT * FROM items_table")
    fun getAlphabetizedWords(): List<Item>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recentAddress: Item)

}
