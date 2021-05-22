package com.tekkr.data.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items_table")
class CartItem(

        @PrimaryKey
        @ColumnInfo(name = "item")
        val item: Int,

        @ColumnInfo(name = "quantity")
        val quantity: Int
)
