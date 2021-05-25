package com.tekkr.data.models


data class SimpleResponse(
        var status: String = STATUS_FAILED,
        var message: String = ""
){
    companion object {

        const val STATUS_SUCCESS: String = "success"
        const val STATUS_FAILED: String = "failed"
        const val MESSAGE: String = "message"
    }
}