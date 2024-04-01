package com.example.tenant_care.network

import com.example.tenant_care.model.PManagerRequestBody
import com.example.tenant_care.model.PManagerResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("pmanager/login")
    suspend fun loginPManager(
        @Body pManagerLoginDetails: PManagerRequestBody
    ) : Response<PManagerResponseBody>
}