package com.tekkr.data.models

data class OrderResponse(

        val status: String,
        val message: String,
        val order: Order
)