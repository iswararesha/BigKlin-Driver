package com.ptckit.bigklin_driver.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: String,
    val username: String,
    val email : String,
    val address : String,
    val nomorHp : String,
    val token: String,
    val isLogin: Boolean
) : Parcelable