package com.tekkr.data.models

import com.tekkr.data.roomDatabase.Address
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.data.roomDatabase.Item


data class OrderItem(
        val id: Int,
        val item_details: BigItem,
        val quantity: Int
) {

    companion object {

        const val STATUS_DELIVERED: String = "DELIVERED"
        const val STATUS_UNDELIVERED: String = "UNDELIVERED"
        const val STATUS_ASSIGNED: String = "ASSIGNED"

    }
}

