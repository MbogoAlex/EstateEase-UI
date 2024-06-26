package com.example.tenant_care.ui.screens.pManagerViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.datastore.UserDSDetails
import com.example.tenant_care.network.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class PManagerHomeScreenUiState(
    val userDSDetails: UserDSDetails = UserDSDetails(0, 0, "", "", "", "", "", ""),
    val childScreen: String = "",
)
@RequiresApi(Build.VERSION_CODES.O)
class PManagerHomeScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = PManagerHomeScreenUiState())
    val uiState: StateFlow<PManagerHomeScreenUiState> = _uiState.asStateFlow()

    private val childScreen: String? = savedStateHandle[PManagerHomeScreenDestination.childScreen]

    // load user details

    fun loadUserDetails() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() { userDsDetails ->
                _uiState.update {
                    it.copy(
                        userDSDetails = userDsDetails,
                        childScreen = childScreen ?: ""
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dsRepository.deleteAllPreferences()
        }
    }

    fun resetChildScreen() {
        _uiState.update {
            it.copy(
                childScreen = ""
            )
        }
    }



    init {
        loadUserDetails()
    }

}