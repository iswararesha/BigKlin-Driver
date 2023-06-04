package com.ptckit.bigklin_driver.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ptckit.bigklin_driver.model.User
import com.ptckit.bigklin_driver.preference.UserPreference

class MainViewModel (private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }
}