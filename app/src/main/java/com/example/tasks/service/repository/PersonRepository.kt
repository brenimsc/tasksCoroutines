package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.BaseRepository
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.R
import com.example.tasks.service.retrofit.api.PersonService
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.retrofit.api.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(val context: Context) : BaseRepository(context) {

    val service = RetrofitClient.createService(PersonService::class.java)

    fun login(email: String, password: String, callback: (headerModel : HeaderModel?, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(null, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<HeaderModel> = service.login(email, password)
        call.enqueue(object : Callback<HeaderModel>{

            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    callback(null, validation)
                } else {
                    response.body()?.let {
                        callback(it, null)
                    }
                }

            }

            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                callback(null, context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun createLogin(name: String, email: String, password: String, callback: (headerModel : HeaderModel?, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(null, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }


        val call: Call<HeaderModel> = service.createLogin(name, email, password)
        call.enqueue(object : Callback<HeaderModel>{

            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    callback(null, validation)
                } else {
                    response.body()?.let {
                        callback(it, null)
                    }
                }

            }

            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                callback(null, context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }
}