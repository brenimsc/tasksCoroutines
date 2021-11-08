package com.example.tasks.service.repository

import android.content.Context
import android.util.Log
import com.example.tasks.BaseRepository
import com.example.tasks.R
import com.example.tasks.extensions.processData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.fingerprint.Fingerprint
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.retrofit.api.RetrofitClient
import com.example.tasks.service.retrofit.api.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class TaskRepository(val context: Context) : BaseRepository(context) {

    private val service = RetrofitClient.createService(TaskService::class.java)

    private suspend fun list(
        responseInt: Int,
        callback: (list: List<TaskModel>?, error: String?) -> Unit
    ) {

        if (!isConnectionAvailable(context)) {
            callback(null, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val response = when (responseInt) {
            TaskConstants.FILTER.ALL -> service.all()
            TaskConstants.FILTER.NEXT -> service.nextWeek()
            else -> service.overdue()
        }

        try {
            response.processData { sucess, error ->
                if (sucess != null) {
                    callback(sucess, null)
                } else {
                    callback(null, error)
                }
            }
        } catch (e: Exception) {
            callback(null, "Houve uma exceção!")
            Log.e("BRENOL", "Excecao ${e.message}")
        }
    }

    suspend fun all(callback: (list: List<TaskModel>?, error: String?) -> Unit) =
        list(TaskConstants.FILTER.ALL, callback)

    suspend fun nextWeek(callback: (list: List<TaskModel>?, error: String?) -> Unit) =
        list(TaskConstants.FILTER.NEXT, callback)

    suspend fun overdue(callback: (list: List<TaskModel>?, error: String?) -> Unit) =
        list(TaskConstants.FILTER.EXPIRED, callback)

    suspend fun updateStatus(
        id: Int,
        complete: Boolean,
        callback: (succes: Boolean, error: String?) -> Unit
    ) {

        if (!isConnectionAvailable(context)) {
            callback(false, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        if (complete) {
            try {
                val response = service.complete(id)
                response.processData { sucess, error ->
                    if (sucess != null) {
                        callback(sucess, null)
                    } else {
                        callback(false, error)
                    }
                }
            } catch (e: Exception) {
                Log.e("BRENOL", e.message.toString())
            }
        } else {
            try {
                val response = service.undo(id)
                response.processData { sucess, error ->
                    if (sucess != null) {
                        callback(sucess, null)
                    } else {
                        callback(false, error)
                    }
                }
            } catch (e: Exception) {
                Log.e("BRENOL", e.message.toString())
            }
        }


    }

    suspend fun create(task: TaskModel, callback: (sucess: Boolean, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(false, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        try {
            val response =
                service.create(task.priorityId, task.description, task.data, task.complete)
            response.processData { sucess, error ->
                if (sucess != null) {
                    callback(sucess, null)
                } else {
                    callback(false, error)
                }
            }
        } catch (e: Exception) {
            Log.e("BRENOL", e.message.toString())
        }
    }

    suspend fun load(taskId: Int, callback: (task: TaskModel?, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(null, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        try {
            val response = service.load(taskId)
            response.processData { sucess, error ->
                if (sucess != null) {
                    callback(sucess, null)
                } else {
                    callback(null, error)
                }
            }
        } catch (e: Exception) {
            Log.e("BRENOL", e.message.toString())
        }
    }

    suspend fun update(task: TaskModel, callback: (sucess: Boolean, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(false, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        try {
            val response =
                service.update(task.id, task.priorityId, task.description, task.data, task.complete)
            response.processData { sucess, error ->
                if (sucess != null) {
                    callback(sucess, null)
                } else {
                    callback(false, error)
                }
            }
        } catch (e: Exception) {
            Log.e("BRENOL", e.message.toString())
        }
    }

    suspend fun delete(id: Int, callback: (sucess: Boolean, error: String?) -> Unit) {

        if (!isConnectionAvailable(context)) {
            callback(false, context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        try {
            val response = service.delete(id)
            response.processData { sucess, error ->
                if (sucess != null) {
                    callback(sucess, null)
                } else {
                    callback(false, error)
                }
            }
        } catch (e: Exception) {
            Log.e("BRENOL", e.message.toString())
        }

    }
}