package com.tekkr.data.models

import com.tekkr.data.roomDatabase.Address


data class Error(

        val code: String,
        val description: String,
        var field: String = "",
        var source: String = "",
        var step: String = "",
        var reason: String = ""
)
