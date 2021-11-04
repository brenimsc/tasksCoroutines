package com.example.tasks.service.retrofit.api

import com.example.tasks.service.constants.TaskConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient() {

    companion object {

        const val BASE_URL = "http://devmasterteam.com/CursoAndroidAPI/"
        private var personKey = ""
        private var tokenKey = ""


        private lateinit var retrofit : Retrofit
        private fun getRetrofit() : Retrofit {
            val client = OkHttpClient.Builder()

            client.addInterceptor {
                val request =
                    it.request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, tokenKey)
                        .build()
                it.proceed(request)
            }

            if (!Companion::retrofit.isInitialized) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build()
            }
            return retrofit
        }

        fun addHeader(token: String, personKey: String) {
            this.personKey = personKey
            tokenKey = token
        }

        fun <S> createService(serviceClass: Class<S>) : S {
            return getRetrofit().create(serviceClass)
        }
    }
}