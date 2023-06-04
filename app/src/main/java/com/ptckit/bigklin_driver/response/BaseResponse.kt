package com.ptckit.bigklin_driver.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @field:SerializedName("message")
    val message: String? = null
)