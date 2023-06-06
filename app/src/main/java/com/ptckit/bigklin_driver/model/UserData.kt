package com.ptckit.bigklin_driver.model

import com.google.gson.annotations.SerializedName

data class UserData(
    @field:SerializedName("id")
    val id: Number,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("alamat")
    val alamat: String,

    @field:SerializedName("nomor_hp")
    val nomorHp: String,

    @field:SerializedName("status_member")
    val status_member: Int
)
