package com.example.tasks.service.retrofit.api

import com.example.tasks.service.model.HeaderModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PersonService {

    @POST("Authentication/Login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String): Response<HeaderModel>



    @POST("Authentication/Create")
    @FormUrlEncoded
    suspend fun createLogin(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("receivenews") receivenews : Boolean = false) : Response<HeaderModel>
}