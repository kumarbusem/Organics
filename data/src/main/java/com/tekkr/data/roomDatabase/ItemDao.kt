package com.tekkr.data.roomDatabase

import androidx.room.*

@Dao
interface ItemDao {

    @Query("SELECT * FROM items_table WHERE active ORDER BY priority DESC")
    fun getAllItems(): List<BigItem>

    @Query("SELECT * FROM items_table WHERE number != 0 AND is_in_stock AND active")
    fun getCartItems(): List<BigItem>

    @Query("UPDATE items_table SET number = 0")
    fun clearCartItems()

    @Query("UPDATE items_table SET active = 0")
    fun makeOfflineItemsInactive()

    @Query("SELECT * from items_table WHERE id= :id")
    fun getItemById(id: Int): List<BigItem?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BigItem)

    @Update(entity = BigItem::class)
    suspend fun update(obj: Item)

    @Update(entity = BigItem::class)
    suspend fun update(obj: CartItem)

}
