package com.ptckit.bigklin_driver.response

import com.google.gson.annotations.SerializedName

class QRCodeResponse (
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: String? = null
)