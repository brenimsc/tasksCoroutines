package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.local.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = SecurityPreferences(application)

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean> get() = _logout



    fun loadUserName() {
        _name.value = preferences.get(TaskConstants.SHARED.PERSON_NAME)
    }

    fun logout() {
        preferences.apply {
            remove(TaskConstants.SHARED.TOKEN_KEY)
            remove(TaskConstants.SHARED.PERSON_KEY)
            remove(TaskConstants.SHARED.PERSON_NAME)

            _logout.value = true
        }
    }
}