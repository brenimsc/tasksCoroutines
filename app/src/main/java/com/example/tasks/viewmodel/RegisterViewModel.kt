package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.Validation
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application)
    private val preferences = SecurityPreferences(application)

    private val _create = MutableLiveData<Validation>()
    val create: LiveData<Validation> get() = _create

    fun create(name: String, email: String, password: String) {
        personRepository.createLogin(name, email, password) { headerModel, error ->
            when {
                onSucess(headerModel) -> {
                    saveInfoUser(headerModel)
                    _create.value = Validation(true)
                }

                onFailure(error) -> {
                    _create.value = Validation(false, error!!)
                }
            }
        }
    }

    private fun onFailure(error: String?) = !error.isNullOrEmpty()

    private fun onSucess(headerModel: HeaderModel?) = headerModel != null

    private fun saveInfoUser(headerModel: HeaderModel?) {
        preferences.store(TaskConstants.SHARED.TOKEN_KEY, headerModel!!.token)
        preferences.store(TaskConstants.SHARED.PERSON_KEY, headerModel.personKey)
        preferences.store(TaskConstants.SHARED.PERSON_NAME, headerModel.name)
    }

}