package com.ptckit.bigklin_driver.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaketLaundry(
    @field:SerializedName("id")
    val id: Number,

    @field:SerializedName("kode_produk")
    val kodePaket: String,

    @field:SerializedName("nama_produk")
    val namaPaket: String,

    @field:SerializedName("harga")
    val harga: Number,
): Parcelable
