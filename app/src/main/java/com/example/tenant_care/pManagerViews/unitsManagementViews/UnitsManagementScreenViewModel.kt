package com.example.tenant_care.pManagerViews.unitsManagementViews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.container.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.model.property.PropertyUnitData
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Screen {
    OCCUPIED_UNITS,
    UNOCCUPIED_UNITS,
    ADD_UNIT
}
enum class FetchingStatus {
    INITIAL,
    FETCHING,
    SUCCESS,
    FAIL
}
data class UnitsManagementScreenUiState(
    val properties: List<PropertyUnit> = emptyList(),
    val fetchingStatus: FetchingStatus = FetchingStatus.INITIAL,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val currentScreen: Screen = Screen.OCCUPIED_UNITS
)
class UnitsManagementScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = UnitsManagementScreenUiState())
    val uiState: StateFlow<UnitsManagementScreenUiState> = _uiState.asStateFlow()

    fun changeScreen(screen: Screen) {
        _uiState.update {
            it.copy(
                currentScreen = screen
            )
        }
    }
    init {

    }

}