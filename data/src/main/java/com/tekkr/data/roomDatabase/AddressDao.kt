package com.tekkr.data.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AddressDao {

    @Query("SELECT * FROM address_table")
    fun getAddresses(): List<Address>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(address: Address)

}
