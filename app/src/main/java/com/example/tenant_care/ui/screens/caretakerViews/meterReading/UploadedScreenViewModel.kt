package com.example.tenant_care.ui.screens.caretakerViews.meterReading

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.util.LoadingStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class UploadedScreenUiState(
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val meterReadings: List<WaterMeterDt> = emptyList(),
    val month: String = "",
    val year: String = "",
    val meterReadingTaken: Boolean = true,
    val tenantName: String? = null,
    val propertyName: String? = null,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL,
)
@RequiresApi(Build.VERSION_CODES.O)
class UploadedScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(value = UploadedScreenUiState())
    val uiState: StateFlow<UploadedScreenUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadStartupData() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserModel ->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserModel.toUserDetails(),
                        year = LocalDateTime.now().year.toString(),
                        month = LocalDateTime.now().month.toString()
                    )
                }
            }
        }
        fetchMeterReadings()
    }

    fun fetchMeterReadings() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.getMeterReadings(
                    month = uiState.value.month,
                    year = uiState.value.year,
                    meterReadingTaken = uiState.value.meterReadingTaken,
                    tenantName = uiState.value.tenantName,
                    propertyName = uiState.value.propertyName
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            meterReadings = response.body()?.data?.waterMeter!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAILURE
                        )
                    }
                    Log.e("METER_READINGS_FETCH_FAIL_RESPONSE", response.toString())
                }

            }catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAILURE
                    )
                }
                Log.e("METER_READINGS_FETCH_FAIL_EXCEPTION", e.toString())
            }
        }
    }

    fun updateMonth(month: String) {
        _uiState.update {
            it.copy(
                month = month
            )
        }
        fetchMeterReadings()
    }

    fun updateYear(year: String) {
        _uiState.update {
            it.copy(
                year = year
            )
        }
        fetchMeterReadings()
    }

    fun updateTenantName(tenantName: String) {
        _uiState.update {
            it.copy(
                tenantName = tenantName
            )
        }
        fetchMeterReadings()
    }

    fun updatePropertyName(propertyName: String) {
        _uiState.update {
            it.copy(
                propertyName = propertyName
            )
        }
        fetchMeterReadings()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun unfilter() {
        _uiState.update {
            it.copy(
                month = LocalDateTime.now().month.toString(),
                year = LocalDateTime.now().year.toString(),
                tenantName = null,
                propertyName = null
            )
        }
        fetchMeterReadings()
    }

    init {
        loadStartupData()
    }
}