package com.ptckit.bigklin_driver.response

import com.google.gson.annotations.SerializedName

data class SnapTokenResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: String
)