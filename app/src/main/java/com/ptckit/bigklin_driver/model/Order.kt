package com.ptckit.bigklin_driver.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    @field:SerializedName("user_id")
    val user_id: String,

    @field:SerializedName("order_id")
    val order_id: String,

    @field:SerializedName("kode_produk")
    val kode_produk: String,

    @field:SerializedName("harga_produk")
    val harga_produk: Int,

    @field:SerializedName("status_pembayaran")
    val status_pembayaran: Int,

    @field:SerializedName("jenis_order")
    val jenis_order: String,

    @field:SerializedName("jarak_pengantaran")
    val jarak_pengantaran: Int,

    @field:SerializedName("harga_antar")
    val harga_antar: Int,

    @field:SerializedName("nama_pelanggan")
    val nama_pelanggan: String,

    @field:SerializedName("alamat_pelanggan")
    val alamat_pelanggan: String,

    @field:SerializedName("nomor_hp_pelanggan")
    val nomor_hp_pelanggan: String,

    @field:SerializedName("latitude")
    val latitude: Double,

    @field:SerializedName("longitude")
    val longitude: Double
): Parcelable