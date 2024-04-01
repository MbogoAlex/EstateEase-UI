package com.example.tenant_care

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.tenant_care.container.AppContainer
import com.example.tenant_care.container.DefaultContainer
import com.example.tenant_care.datastore.DSRepository

private const val DS_NAME = "ESTATE_EASE_DS"
private val Context.datastore: DataStore<Preferences> by preferencesDataStore(
    name = DS_NAME
)
class EstateEase: Application() {
    lateinit var container: AppContainer
    lateinit var dsRepository: DSRepository
    override fun onCreate() {
        super.onCreate()
        container = DefaultContainer(this)
        dsRepository = DSRepository(datastore)
    }
}