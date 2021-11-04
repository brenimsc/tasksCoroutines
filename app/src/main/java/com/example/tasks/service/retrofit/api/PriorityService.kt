package com.example.tasks.service.retrofit.api

import com.example.tasks.service.model.PriorityModel
import retrofit2.Call
import retrofit2.http.GET

interface PriorityService {

    @GET("Priority")
    fun list() : Call<List<PriorityModel>>

}