package com.example.tasks.service.retrofit.api

import com.example.tasks.service.model.PriorityModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface PriorityService {

    @GET("Priority")
    suspend fun list() : Response<List<PriorityModel>>

}