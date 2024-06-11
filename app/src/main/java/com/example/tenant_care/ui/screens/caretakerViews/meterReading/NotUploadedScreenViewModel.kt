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

data class NotUploadedScreenUiState(
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val meterReadings: List<WaterMeterDt> = emptyList(),
    val month: String = "",
    val year: String = "",
    val months: List<String> = emptyList(),
    val years: List<String> = emptyList(),
    val roomName: String = "",
    val tenantName: String = "",
    val selectableRooms: List<String> = emptyList(),
    val filteringOn: Boolean = false,
    val meterReadingTaken: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL,
)
@RequiresApi(Build.VERSION_CODES.O)
class NotUploadedScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = NotUploadedScreenUiState())
    val uiState: StateFlow<NotUploadedScreenUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadStartupData() {
        val months = listOf<String>(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )
        var currentYear = LocalDateTime.now().year
        val years = mutableListOf<String>()
        var i = 10
        do {
            years.add(currentYear.toString())
            currentYear--
            i--
        } while (i > 0)
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserModel ->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserModel.toUserDetails(),
                        year = LocalDateTime.now().year.toString(),
                        month = LocalDateTime.now().month.toString(),
                        months = months,
                        years = years
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
                    propertyName = uiState.value.roomName,
                    role = "caretaker"
                )
                if(response.isSuccessful) {
                    val rooms = mutableListOf<String>()
                    for(reading in response.body()?.data?.waterMeter!!) {
                        rooms.add(reading.propertyName!!)
                    }
                    _uiState.update {
                        it.copy(
                            meterReadings = response.body()?.data?.waterMeter!!,
                            selectableRooms = rooms,
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

    fun updateRoomName(roomName: String) {
        _uiState.update {
            it.copy(
                roomName = roomName,
                filteringOn = true
            )
        }
        fetchMeterReadings()
    }

    fun updateTenantName(tenantName: String) {
        _uiState.update {
            it.copy(
                tenantName = tenantName,
                filteringOn = true
            )
        }
        fetchMeterReadings()
    }

    fun unfilter() {
        _uiState.update {
            it.copy(
                tenantName = "",
                roomName = "",
                month = LocalDateTime.now().month.toString(),
                year = LocalDateTime.now().year.toString(),
                filteringOn = false
            )
        }
        fetchMeterReadings()
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



    init {
        loadStartupData()
    }
}