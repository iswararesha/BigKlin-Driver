package com.ptckit.bigklin_driver.model

import com.google.gson.annotations.SerializedName

data class DataLog (

    @field:SerializedName("id")
    val id: Number,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("est")
    val est: String

)
