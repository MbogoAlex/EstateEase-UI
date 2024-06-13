package com.example.tenant_care.container

import android.content.Context
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.network.ApiService
import com.example.tenant_care.network.NetworkRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val apiRepository: ApiRepository
}

class DefaultContainer(context: Context): AppContainer {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    private val baseUrl = "https://e984-154-122-183-155.ngrok-free.app/api/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    override val apiRepository: ApiRepository by lazy {
        NetworkRepository(retrofitService)
    }
}


