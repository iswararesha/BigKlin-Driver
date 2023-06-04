package com.ptckit.bigklin_driver.response

import com.google.gson.annotations.SerializedName
import com.ptckit.bigklin_driver.model.PaketLaundry

data class PaketLaundryResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val paketLaundry: List<PaketLaundry>,
)