package com.example.tenant_care.ui.screens.tenantViews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TenantHomeScreenUiState(
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
)
class TenantHomeScreenViewModel(
    private val dsRepository: DSRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(value = TenantHomeScreenUiState())
    val uiState: StateFlow<TenantHomeScreenUiState> = _uiState.asStateFlow()

    fun loadUserDetails() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserDetails ->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails()
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

    init {
        loadUserDetails()
    }
}