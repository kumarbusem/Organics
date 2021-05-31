package com.tekkr.data.roomDatabase

import androidx.room.*

@Dao
interface ItemDao {

    @Query("SELECT * FROM items_table ORDER BY priority DESC")
    fun getAllItems(): List<BigItem>

    @Query("SELECT * FROM items_table WHERE number != 0 AND is_in_stock")
    fun getCartItems(): List<BigItem>

    @Query("SELECT * from items_table WHERE id= :id")
    fun getItemById(id: Int): List<BigItem?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BigItem)

    @Update(entity = BigItem::class)
    suspend fun update(obj: Item)

    @Update(entity = BigItem::class)
    suspend fun update(obj: CartItem)

}
