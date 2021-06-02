package com.tekkr.data.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "items_table")
data class Item(

        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Int,

        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "image_url")
        val image_url: String,

        @ColumnInfo(name = "category")
        val category: Int,

        @ColumnInfo(name = "category_name")
        val category_name: String,

        @ColumnInfo(name = "quantity")
        val quantity: Int,

        @ColumnInfo(name = "unit")
        val unit: String,

        @ColumnInfo(name = "item_price")
        val item_price: Int,

        @ColumnInfo(name = "is_in_stock")
        val is_in_stock: Boolean,

        @ColumnInfo(name = "vendor")
        val vendor: Int,

        @ColumnInfo(name = "vendor_name")
        val vendor_name: String,

        @ColumnInfo(name = "priority")
        val priority: Int,

        @ColumnInfo(name = "active")
        val active: Boolean,

)
