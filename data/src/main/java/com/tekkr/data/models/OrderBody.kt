package com.tekkr.data.models

import com.tekkr.data.roomDatabase.Address


data class OrderBody(

        val is_new_address: Boolean,
        val delivery_address: Address,
        val customer_id: Int,
        val order_items: List<CartData>
)

