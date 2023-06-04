package com.ptckit.bigklin_driver.response

import com.google.gson.annotations.SerializedName
import com.ptckit.bigklin_driver.model.UserOrder

data class UserOrderResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: UserOrder
)