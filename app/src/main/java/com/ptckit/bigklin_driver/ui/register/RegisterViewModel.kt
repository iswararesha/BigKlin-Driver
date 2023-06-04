package com.ptckit.bigklin_driver.ui.register

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.ptckit.bigklin_driver.api.base.ApiConfig
import com.ptckit.bigklin_driver.model.User
import com.ptckit.bigklin_driver.preference.UserPreference
import com.ptckit.bigklin_driver.response.BaseResponse
import com.ptckit.bigklin_driver.response.RegisterResponse
import com.ptckit.bigklin_driver.response.UserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel (private val pref: UserPreference) : ViewModel() {
    private val client = ApiConfig.apiService()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun register(name: String, email: String, password: String, address: String, phone: String){
        _isLoading.value = true

        client.register(name, email, password, address, phone).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    Log.e(ContentValues.TAG, response.body().toString())

                    val token = response.body()?.type + " " + response.body()?.token
                    token?.let { getUser(it) }

                } else {
                    val baseResponse = Gson().fromJson(response.errorBody()!!.charStream(),
                        BaseResponse::class.java)

                    _message.value = baseResponse.message!!
                    Log.e(ContentValues.TAG, "onFailure: ${baseResponse.message}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun getUser(token: String){
        _isLoading.value = true

        client.getUserData(token).enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    Log.e(ContentValues.TAG, response.body().toString())

                    val username = response.body()?.data?.name
                    val email = response.body()?.data?.email
                    val address = response.body()?.data?.alamat
                    val userId = response.body()?.data?.id.toString()
                    val nomorHp = response.body()?.data?.nomorHp

                    val userData = User(
                        userId!!,
                        username!!,
                        email!!,
                        address!!,
                        nomorHp!!,
                        token!!,
                        true
                    )

                    viewModelScope.launch {
                        pref.login(userData)
                    }
                } else {
                    val baseResponse = Gson().fromJson(response.errorBody()!!.charStream(),
                        BaseResponse::class.java)

                    _message.value = baseResponse.message!!
                    Log.e(ContentValues.TAG, "onFailure: ${baseResponse.message}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}