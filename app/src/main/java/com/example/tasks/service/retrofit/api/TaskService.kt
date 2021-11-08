package com.example.tasks.service.retrofit.api

import com.example.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface TaskService {

    @GET("Task")
    suspend fun all() : retrofit2.Response<List<TaskModel>>

    @GET("Task/Next7Days")
    suspend fun nextWeek() : Response<List<TaskModel>>

    @GET("Task/Overdue")
    suspend fun overdue() : Response<List<TaskModel>>

    @GET("Task/{id}") //encoded caso tenha espaco ele é encapsulado para que api n quebre
    suspend fun load(@Path(value = "id", encoded = true) id: Int) : Response<TaskModel>

    @POST("Task")
    @FormUrlEncoded
    suspend fun create(
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete : Boolean
    ) : Response<Boolean>

    //@PUT("Task")
    @HTTP(method = "PUT", path = "Task", hasBody = true) //Como precisamos passar valores tem que ser assim , se n poderia ser o outro PUT e ser passado no Body
    @FormUrlEncoded
    suspend fun update(
        @Field("Id") id: Int,
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete : Boolean
    ) : Response<Boolean>

    @HTTP(method = "PUT", path = "Task/Complete", hasBody = true)
    @FormUrlEncoded
    suspend fun complete(@Field("Id") id: Int) : Response<Boolean>

    @HTTP(method = "PUT", path = "Task/Undo", hasBody = true)
    @FormUrlEncoded
    suspend fun undo(@Field("Id") id: Int) : Response<Boolean>

    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    @FormUrlEncoded
    suspend fun delete(@Field("Id") id: Int) : Response<Boolean>


}