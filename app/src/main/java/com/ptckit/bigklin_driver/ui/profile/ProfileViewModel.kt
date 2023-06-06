package com.ptckit.bigklin_driver.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ptckit.bigklin_driver.model.User
import com.ptckit.bigklin_driver.preference.UserPreference
import kotlinx.coroutines.launch

class ProfileViewModel (private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }

}