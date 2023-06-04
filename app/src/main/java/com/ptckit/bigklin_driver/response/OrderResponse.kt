package com.ptckit.bigklin_driver.response

import com.google.gson.annotations.SerializedName
import com.ptckit.bigklin_driver.model.Order

class OrderResponse (
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<Order>? = null
)