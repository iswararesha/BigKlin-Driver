package com.ptckit.bigklin_driver.ui.home

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.ptckit.bigklin_driver.api.base.ApiConfig
import com.ptckit.bigklin_driver.model.Order
import com.ptckit.bigklin_driver.model.User
import com.ptckit.bigklin_driver.preference.UserPreference
import com.ptckit.bigklin_driver.response.BaseResponse
import com.ptckit.bigklin_driver.response.OrderResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel (private val pref: UserPreference) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message : LiveData<String> = _message

    private val _orderData = MutableLiveData<List<Order>>()
    val orderData : LiveData<List<Order>> = _orderData

    private val client = ApiConfig.apiService()

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getListOrder(token: String){
        _isLoading.value = true

        client.getAllOrder(token).enqueue(object: Callback<OrderResponse>{
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    Log.e(ContentValues.TAG, response.body().toString())

                    _message.value = response.body()?.message
                    _orderData.value = response.body()?.data
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

    fun getSuccessOrder(token: String){
        _isLoading.value = true

        client.getSuccessOrder(token).enqueue(object: Callback<OrderResponse>{
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    Log.e(ContentValues.TAG, response.body().toString())

                    _message.value = response.body()?.message
                    _orderData.value = response.body()?.data
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
}