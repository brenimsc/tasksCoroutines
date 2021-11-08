package com.example.tasks.extensions

import com.google.gson.Gson
import retrofit2.Response

fun <T> Response<T>.processData(callback: (sucess: T?, error: String?) -> Unit) {
    if (this.isSuccessful) {
        this.body()?.let { body ->
            callback(body, null)
        }
    } else {
        val validation = Gson().toJson(this.errorBody()!!.string(), String::class.java)
        callback(null, validation)
    }
}
