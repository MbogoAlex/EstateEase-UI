package com.example.tenant_care.pManagerViews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.container.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.datastore.UserDSDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstateEaseRentOverview(
    val totalExpectedRent: Double = 0.0,
    val totalUnits: Int = 0,
    val paidAmount: Double = 0.0,
    val clearedUnits: Int = 0,
    val deficit: Double = 0.0,
    val unclearedUnits: Int = 1
)

data class PManagerHomeScreenUiState(
    val estateEaseRentOverview: EstateEaseRentOverview = EstateEaseRentOverview(),
    val userDSDetails: UserDSDetails = UserDSDetails(0, 0, "", "", "", "")
)
class PManagerHomeScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(value = PManagerHomeScreenUiState())
    val uiState: StateFlow<PManagerHomeScreenUiState> = _uiState.asStateFlow()

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
    }

    fun fetchRentOverview() {
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchRentPaymentOverview("April", "2024")
                if(response.isSuccessful) {
                    val estateEaseRentOverview = EstateEaseRentOverview(
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

    init {
        loadUserDetails()
        fetchRentOverview()
    }

}