package com.example.tenant_care.container

import com.example.tenant_care.model.PManagerRequestBody
import com.example.tenant_care.model.PManagerResponseBody
import com.example.tenant_care.network.ApiService
import retrofit2.Response

interface ApiRepository {
    suspend fun loginPManager(pManagerLoginDetails: PManagerRequestBody) : Response<PManagerResponseBody>
}

class NetworkRepository(private val apiService: ApiService): ApiRepository {
    override suspend fun loginPManager(pManagerLoginDetails: PManagerRequestBody): Response<PManagerResponseBody> = apiService.loginPManager(pManagerLoginDetails)

}