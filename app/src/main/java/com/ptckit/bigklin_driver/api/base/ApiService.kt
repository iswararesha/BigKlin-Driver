package com.ptckit.bigklin_driver.api.base

import com.ptckit.bigklin_driver.model.Order
import com.ptckit.bigklin_driver.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    @Headers("Authorization: token 12345")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("alamat") alamat: String,
        @Field("nomor_hp") nomor_hp: String,
    ) : Call<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    @Headers("Authorization: token 12345")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ) : Call<LoginResponse>

    @FormUrlEncoded
    @POST("auth/reset-password")
    @Headers("Authorization: token 12345")
    fun reset_password(
        @Field("email") email: String,
    ) : Call<ResetPasswordResponse>

    @GET("auth/logout")
    fun logout(
        @Header("Authorization") token: String,
    ) : Call<UserResponse>

    @GET("get-produk")
    fun getPaketLaundry(): Call<PaketLaundryResponse>

    @FormUrlEncoded
    @POST("drop-off/store")
    fun order(
        @Header("Authorization") token: String,
        @Field("kode_produk") kode_produk: String,
        @Field("jarak_pengantaran") jarak_pengantaran: Int,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ) : Call<BaseResponse>

    @GET("user")
    fun getUserOrder(
        @Header("Authorization") token: String,
    ) : Call<UserOrderResponse>

    @GET("kurir/get-all-order")
    fun getAllOrder(
        @Header("Authorization") token: String,
    ) : Call<OrderResponse>

    @GET("get-activity-log/{user_id}")
    fun getActivityLog(
        @Field("id") id: Number,
    ) : Call<ActivityLogResponse>

    @GET("get-snap-token/{kode_produk}")
    fun getSnapToken(
        @Header("Authorization") token: String,
        @Path("kode_produk") kode_getproduk: String,
    ) : Call<SnapTokenResponse>

    @GET("kurir/get-order/{order_id}")
    fun getOrderByID(
        @Header("Authorization") token: String,
        @Path("order_id") order_id: String,
    ) : Call<OrderResponse>

    @GET("user")
    fun getUserData(
        @Header("Authorization") token: String,
    ) : Call<UserResponse>

    @FormUrlEncoded
    @POST("kurir/cash/{order_id}")
    fun setPaymentCash(
        @Header("Authorization") token: String,
        @Path("order_id") order_id: String,
        @Field("nilai_timbang") nilai_timbang: Int
    ) : Call<OrderResponse>

    @FormUrlEncoded
    @POST("kurir/get-barcode-payment/{order_id}")
    fun setPaymentDigital(
        @Header("Authorization") token: String,
        @Path("order_id") order_id: String,
        @Field("nilai_timbang") nilai_timbang: Int
    ) : Call<QRCodeResponse>
}