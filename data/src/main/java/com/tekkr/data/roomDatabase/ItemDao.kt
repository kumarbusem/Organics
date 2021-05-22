package com.tekkr.data.roomDatabase

import androidx.room.*

@Dao
interface ItemDao {

    @Query("SELECT * FROM items_table")
    fun getAllItems(): List<BigItem>

    @Query("SELECT * from items_table WHERE id= :id")
    fun getItemById(id: Int): List<BigItem?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentAddress: BigItem)

    @Update(entity = BigItem::class)
    suspend fun update(obj: Item)

    @Update(entity = BigItem::class)
    suspend fun update(obj: CartItem)

}