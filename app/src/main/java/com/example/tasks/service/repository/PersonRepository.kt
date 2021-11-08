package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.BaseRepository
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.R
import com.example.tasks.extensions.processData
import com.example.tasks.service.retrofit.api.PersonService
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.retrofit.api.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(val context: Context) : BaseRepository(context) {

    val service = RetrofitClient.createService(PersonService::class.java)

    suspend fun login(email: String, password: String, callback: (headerModel : HeaderModel?, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(null, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val response = service.login(email, password)
        response.processData { sucess, error ->
            if (sucess != null) {
                response.body()?.let {
                    callback(it, null)
                }
            } else {
                val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                callback(null, validation)
            }
        }
    }

    suspend fun createLogin(name: String, email: String, password: String, callback: (headerModel : HeaderModel?, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(null, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val response = service.createLogin(name, email, password)
        response.processData { sucess, error ->
            if (sucess != null) {
                response.body()?.let {
                    callback(it, null)
                }
            } else {
                val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                callback(null, validation)
//                callback(null, context.getString(R.string.ERROR_UNEXPECTED))
            }
        }
    }
}