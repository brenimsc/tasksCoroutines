package com.example.tasks.service.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.tasks.BaseRepository
import com.example.tasks.extensions.processData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.retrofit.api.PriorityService
import com.example.tasks.service.retrofit.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) : BaseRepository(context) {

    private val service = RetrofitClient.createService(PriorityService::class.java)
    private val database = TaskDatabase.getDatabase(context).priorityDao()

    suspend fun all() {

        if (!isConnectionAvailable(context)) {
            return
        }

        val response = service.list()
        response.processData { sucess, _ ->
            if (sucess != null) {
                database.clear()
                response.body()?.let { database.save(it) }
            }
        }
    }

    fun listPriority() = database.getPriority()

    fun getDescription(id: Int) = database.getDescription(id)


}