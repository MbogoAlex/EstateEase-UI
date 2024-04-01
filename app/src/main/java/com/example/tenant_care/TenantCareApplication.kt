package com.example.tenant_care

import android.app.Application
import com.example.tenant_care.container.AppContainer
import com.example.tenant_care.container.DefaultContainer

class TenantCareApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultContainer(this)
    }
}