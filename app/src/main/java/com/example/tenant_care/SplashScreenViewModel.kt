package com.example.tenant_care

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.datastore.UserDSDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class InitializingStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAIL
}

data class SplashScreenUIState(
    val initializingStatus: InitializingStatus = InitializingStatus.INITIAL,
    val userDSDetails: UserDSDetails = UserDSDetails(0, 0, "", "", "", "", "", "")
)

class SplashScreenViewModel(
    private val dsRepository: DSRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(value = SplashScreenUIState())
    val uiState: StateFlow<SplashScreenUIState> = _uiState.asStateFlow()


    // load user details

    fun loadUserDetails() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() { userDsDetails ->
                _uiState.update {
                    it.copy(
                        userDSDetails = userDsDetails,
                        initializingStatus = InitializingStatus.SUCCESS
                    )
                }
            }
        }
    }

    fun resetInitializationStatus() {
        _uiState.update {
            it.copy(
                initializingStatus = InitializingStatus.INITIAL
            )
        }
    }

    init {
        loadUserDetails()
    }
}