package com.tekkr.data.models


data class Order(
        val ration_id: String,
        val order_id: String,
        val is_active: Boolean,
        val order_slug: String,
        val created_date: String,
        val created_time: String,
        val status: String,
        val customer_name: String,
        val customer_mobile: String,
        val address: String,
        val locality: String,
        val no_of_members: String,
        val father_name: String,
        val mother_name: String,
        val spouse_name: String,
        val fps_lic_no: String,
        val fps_code: String,
        val fps_shop_name: String,
        val opt_for_wheat: String,
        val items: Items = Items()
) {

    companion object {

        const val STATUS_DELIVERED: String = "DELIVERED"
        const val STATUS_UNDELIVERED: String = "UNDELIVERED"
        const val STATUS_ASSIGNED: String = "ASSIGNED"

    }
}

