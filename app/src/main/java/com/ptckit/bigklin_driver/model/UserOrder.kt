package com.ptckit.bigklin_driver.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserOrder(
    @field:SerializedName("order_id")
    val order_id: String,

    @field:SerializedName("status_order")
    val status_order: Int
): Parcelable