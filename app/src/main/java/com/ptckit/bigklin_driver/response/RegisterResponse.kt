package com.ptckit.bigklin_driver.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("access_token")
    val token: String,

    @field:SerializedName("token_type")
    val type: String,
)