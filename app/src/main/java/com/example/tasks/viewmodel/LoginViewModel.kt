package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.Validation
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.fingerprint.Fingerprint
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.retrofit.api.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application)
    private val priorityRepository = PriorityRepository(application)
    private val preferences = SecurityPreferences(application)

    private val _login = MutableLiveData<Validation>()
    val login: LiveData<Validation> get() = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser : LiveData<Boolean> get() = _loggedUser

    private val _fingerprint = MutableLiveData<Boolean>()
    val fingerprint: LiveData<Boolean> get() = _fingerprint


    fun doLogin(email: String, password: String) {
        personRepository.login(email, password) { headerModel, error ->
            when {
                onSucess(headerModel) -> {
                    saveInfoUser(headerModel)
                    headerModel?.let { RetrofitClient.addHeader(it.token, headerModel.personKey) }
                    _login.value = Validation(true)
                }
                onFailure(error) -> {
                    _login.value = Validation(false, error!!)
                }
            }

        }
    }

    private fun saveInfoUser(headerModel: HeaderModel?) {
        preferences.store(TaskConstants.SHARED.TOKEN_KEY, headerModel!!.token)
        preferences.store(TaskConstants.SHARED.PERSON_KEY, headerModel.personKey)
        preferences.store(TaskConstants.SHARED.PERSON_NAME, headerModel.name)
    }

    private fun onFailure(error: String?) = !error.isNullOrEmpty()

    private fun onSucess(headerModel: HeaderModel?) = headerModel != null


    fun verifyLogged() {
        val token = preferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person = preferences.get(TaskConstants.SHARED.PERSON_KEY)
        _loggedUser.value =  (token.isNotEmpty() && person.isNotEmpty())
    }


    fun isAuthenticationAvaliable() {

        val token = preferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person = preferences.get(TaskConstants.SHARED.PERSON_KEY)
        RetrofitClient.addHeader(token, person)

        val logged = (token.isNotEmpty() && person.isNotEmpty())

        if (!logged) {
            priorityRepository.all()
        }

        if (Fingerprint.isAuthenticationAvaliable(getApplication())) {
            _fingerprint.value = logged
        }
    }

}