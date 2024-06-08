package com.example.tenant_care.ui.screens.pManagerViews.rentPayment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.datastore.UserDSDetails
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.ui.screens.pManagerViews.PManagerHomeScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class EstateEaseRentOverview(
    val totalExpectedRent: Double = 0.0,
    val totalUnits: Int = 0,
    val paidAmount: Double = 0.0,
    val clearedUnits: Int = 0,
    val deficit: Double = 0.0,
    val unclearedUnits: Int = 1
)

data class RentPaymentsInfoHomeScreenUiState(
    val estateEaseRentOverview: EstateEaseRentOverview = EstateEaseRentOverview(),
    val userDSDetails: UserDSDetails = UserDSDetails(0, 0, "", "", "", "", "", ""),
    val penaltyStatus: Boolean = false
)
@RequiresApi(Build.VERSION_CODES.O)
class RentPaymentsInfoHomeScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(value = RentPaymentsInfoHomeScreenUiState())
    val uiState: StateFlow<RentPaymentsInfoHomeScreenUiState> = _uiState.asStateFlow()

    // load user details

    fun loadUserDetails() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() { userDsDetails ->
                _uiState.update {
                    it.copy(
                        userDSDetails = userDsDetails,
                    )
                }
            }
        }
        fetchPenalty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchRentOverview() {
        viewModelScope.launch {
            try {
                val currentMonth = LocalDateTime.now().month.toString()

                val response = apiRepository.fetchRentPaymentOverview(currentMonth, "2024")
                if(response.isSuccessful) {
                    val estateEaseRentOverview =
                        EstateEaseRentOverview(
                            totalExpectedRent = response.body()?.data?.rentpayment?.totalExpectedRent!!,
                            totalUnits = response.body()?.data?.rentpayment?.totalUnits!!,
                            paidAmount = response.body()?.data?.rentpayment?.paidAmount!!,
                            clearedUnits = response.body()?.data?.rentpayment?.clearedUnits!!,
                            deficit = response.body()?.data?.rentpayment?.deficit!!,
                            unclearedUnits = response.body()?.data?.rentpayment?.unclearedUnits!!
                        )
                    _uiState.update {
                        it.copy(
                            estateEaseRentOverview = estateEaseRentOverview
                        )
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun fetchPenalty() {
        viewModelScope.launch {
            try {
               val response = apiRepository.fetchPenalty(2)
               if(response.isSuccessful) {
                   _uiState.update {
                       it.copy(
                           penaltyStatus = response.body()?.data?.penalty?.status!!
                       )
                   }
               }
            } catch (e: Exception) {

            }
        }
    }

    fun activateLatePaymentPenalty() {
        viewModelScope.launch {
            try {
                val response = apiRepository.activateLatePaymentPenalty(
                    month = LocalDateTime.now().month.toString(),
                    year = LocalDateTime.now().year.toString()
                )
            } catch (e: Exception) {

            }
        }
    }

    fun deActivateLatePaymentPenalty() {
        viewModelScope.launch {
            try {
                val response = apiRepository.deActivateLatePaymentPenalty(
                    month = LocalDateTime.now().month.toString(),
                    year = LocalDateTime.now().year.toString()
                )
            } catch (e: Exception) {

            }
        }
    }

    init {
        loadUserDetails()
        fetchRentOverview()
    }
}