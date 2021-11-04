package com.example.tasks.service.repository

import android.content.Context
import android.util.Log
import com.example.tasks.BaseRepository
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.retrofit.api.RetrofitClient
import com.example.tasks.service.retrofit.api.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context: Context) : BaseRepository(context) {

    private val service = RetrofitClient.createService(TaskService::class.java)

    private fun list(call: Call<List<TaskModel>>, callback: (list: List<TaskModel>?, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(null, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        call.enqueue(object : Callback<List<TaskModel>>{
            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().toJson(response.errorBody()!!.string(), String::class.java)
                    callback(null, validation)
                } else {
                    response.body()?.let { callback(it, null) }
                }
            }

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                callback(null, context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun all(callback: (list: List<TaskModel>?, error: String?) -> Unit) {
        val call = service.all()
        list(call, callback)
    }

    fun updateStatus(id: Int, complete: Boolean, callback: (succes: Boolean, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(false, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = if (complete) {
            service.complete(id)
        } else {
            service.undo(id)
        }

        call.enqueue(object  : Callback<Boolean> {

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().toJson(response.errorBody()!!.string(), String::class.java)
                    callback(false, validation)
                } else {
                    response.body()?.let { callback(it, null) }
                }

            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback(false, context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun nextWeek(callback: (list: List<TaskModel>?, error: String?) -> Unit) {
        val call = service.nextWeek()
        list(call, callback)
    }

    fun overdue(callback: (list: List<TaskModel>?, error: String?) -> Unit) {
        val call = service.overdue()
        list(call, callback)
    }

    fun create(task: TaskModel, callback: (sucess: Boolean, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(false, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> =
            service.create(task.priorityId, task.description, task.data, task.complete)
        call.enqueue(object  : Callback<Boolean> {

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().toJson(response.errorBody()!!.string(), String::class.java)
                    callback(false, validation)
                } else {
                    response.body()?.let { callback(it, null) }
                }

            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback(false, context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun load(taskId: Int, callback: (sucess: TaskModel?, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(null, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<TaskModel> =
            service.load(taskId)
        call.enqueue(object  : Callback<TaskModel> {

            override fun onResponse(call: Call<TaskModel>, response: Response<TaskModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().toJson(response.errorBody()!!.string(), String::class.java)
                    callback(null, validation)
                } else {
                    response.body()?.let { callback(it, null) }
                }

            }

            override fun onFailure(call: Call<TaskModel>, t: Throwable) {
                callback(null, context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun update(task: TaskModel, callback: (sucess: Boolean, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(false, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> =
            service.update(task.id, task.priorityId, task.description, task.data, task.complete)
        call.enqueue(object  : Callback<Boolean> {

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().toJson(response.errorBody()!!.string(), String::class.java)
                    callback(false, validation)
                } else {
                    response.body()?.let { callback(it, null) }
                }

            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback(false, context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun delete(id: Int, callback: (sucess: Boolean, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(false, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> =
            service.delete(id)
        call.enqueue(object  : Callback<Boolean> {

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().toJson(response.errorBody()!!.string(), String::class.java)
                    callback(false, validation)
                } else {
                    response.body()?.let { callback(it, null) }
                }

            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback(false, context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }
}