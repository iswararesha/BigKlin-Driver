package com.ptckit.bigklin_driver.ui.detail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.gson.Gson
import com.ptckit.bigklin_driver.api.base.ApiConfig
import com.ptckit.bigklin_driver.model.Order
import com.ptckit.bigklin_driver.model.User
import com.ptckit.bigklin_driver.preference.UserPreference
import com.ptckit.bigklin_driver.response.BaseResponse
import com.ptckit.bigklin_driver.response.OrderResponse
import com.ptckit.bigklin_driver.response.QRCodeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel (private val pref: UserPreference) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message : LiveData<String> = _message

    private val _QRLink = MutableLiveData<String>()
    val QRLink : LiveData<String> = _QRLink

    private val client = ApiConfig.apiService()

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun setPaymentCash(token: String, orderId: String, berat: Int){
        _isLoading.value = true

        client.setPaymentCash(token, orderId, berat).enqueue(object: Callback<OrderResponse>{
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    Log.e(ContentValues.TAG, response.body().toString())

                    _message.value = response.body()?.message
                } else {
                    val baseResponse = Gson().fromJson(response.errorBody()!!.charStream(),
                        BaseResponse::class.java)

                    _message.value = baseResponse.message!!
                    Log.e(ContentValues.TAG, "onFailure: ${baseResponse.message}")
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun setPaymentDigital(token: String, orderId: String, berat: Int){
        _isLoading.value = true

        client.setPaymentDigital(token, orderId, berat).enqueue(object: Callback<QRCodeResponse>{
            override fun onResponse(call: Call<QRCodeResponse>, response: Response<QRCodeResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    Log.e(ContentValues.TAG, response.body().toString())

                    _QRLink.value = response.body()?.data
                } else {
                    val baseResponse = Gson().fromJson(response.errorBody()!!.charStream(),
                        BaseResponse::class.java)

                    _message.value = baseResponse.message!!
                    Log.e(ContentValues.TAG, "onFailure: ${baseResponse.message}")
                }
            }

            override fun onFailure(call: Call<QRCodeResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}