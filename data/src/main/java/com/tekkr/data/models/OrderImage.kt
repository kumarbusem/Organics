package com.tekkr.data.models

import android.graphics.Bitmap


data class OrderImage(
        var id: Int = 0,
        var status: String = STATUS_UPLOAD,
        var type: String = ORDER_IMAGE_TYPE_RATION,
        var img: Bitmap? = null
) {

    companion object {
        const val STATUS_SUCCESS: String = "SUCCESS"
        const val STATUS_UPLOAD: String = "UPLOAD"
        const val ORDER_IMAGE_TYPE_BILL: String = "Bill Image"
        const val ORDER_IMAGE_TYPE_RATION: String = "Image"
    }


}