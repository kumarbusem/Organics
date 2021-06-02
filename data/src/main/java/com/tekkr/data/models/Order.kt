package com.tekkr.data.models

import com.tekkr.data.roomDatabase.Address


data class Order(
        val id: Int,
        val order_id: String,
        val status: String,
        val customer: Customer,
        val order_created_time: String,
        val delivery_address: Address,
        val order_items: List<OrderItem>,
        val order_delivered_datetime: String,
        val payment_type: String,
        val total: Float,
        val referral_code: String,
        val razorpay_order_id: String,
        val razorpay_payment_link_id: String,
        val payment_verified: Boolean,

        var detail: String = "",
        var code: String = ""
) {

    companion object {

        const val STATUS_CREATED: String = "CREATED"
        const val STATUS_PACKED: String = "PACKED"

    }
}

