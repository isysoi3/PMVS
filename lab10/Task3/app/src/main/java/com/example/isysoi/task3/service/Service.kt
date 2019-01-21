package com.example.isysoi.task3.service

import com.example.isysoi.task3.model.Singer
import retrofit2.Call
import retrofit2.http.GET


interface Service {
    @GET("singers")
    fun getSingeres(): Call<List<Singer>>
}