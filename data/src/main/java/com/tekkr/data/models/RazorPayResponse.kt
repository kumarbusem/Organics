package com.tekkr.data.models

import com.razorpay.PaymentData


data class RazorPayResponse(

        var status: String = "",
        var paymentData: PaymentData?,
        val error: Error = Error(),

        val code: String = "",
        val description: String = "",
        var field: String = "",
        var source: String = "",
        var step: String = "",
        var reason: String = ""

){
    companion object {

        const val STATUS_SUCCESS: String = "success"
        const val STATUS_FAILED: String = "failed"
        const val STATUS_ALREADY_PAID: String = "STATUS_ALREADY_PAID"
    }
}

