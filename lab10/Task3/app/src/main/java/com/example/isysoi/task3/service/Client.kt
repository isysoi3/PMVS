package com.example.isysoi.task3.service

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val RADIO_BASE_URL = "https://my-json-server.typicode.com/isysoi3/singers_api/"

object Client {
    var service: Service

    init {
        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()

        val httpClient = OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val originalHttpUrl = original.url()
                val url = originalHttpUrl.newBuilder()
                    .build()
                val requestBuilder = original.newBuilder()
                    .url(url)
                val request = requestBuilder.build()
                it.proceed(request)
            }
            .build()
        val retrofit = Retrofit.Builder().baseUrl(RADIO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
        service = retrofit.create(Service::class.java)
    }
}
