package com.ptckit.bigklin_driver.response

import com.google.gson.annotations.SerializedName
import com.ptckit.bigklin_driver.model.UserData

data class UserResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: UserData
)