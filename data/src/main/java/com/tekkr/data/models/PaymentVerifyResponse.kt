package com.tekkr.data.models


data class PaymentVerifyResponse(
        var status: String = "",
        var message: String = ""
){
    companion object {

        const val STATUS_SUCCESS: String = "success"
        const val STATUS_FAILED: String = "failed"
        const val MESSAGE: String = "message"
    }
}