package com.ptckit.bigklin_driver.response

import com.google.gson.annotations.SerializedName
import com.ptckit.bigklin_driver.model.DataLog

data class ActivityLogResponse(
    @field:SerializedName("data")
    val dataLog: List<DataLog>,
)