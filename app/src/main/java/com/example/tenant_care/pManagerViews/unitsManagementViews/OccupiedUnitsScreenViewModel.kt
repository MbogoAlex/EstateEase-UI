package com.example.tenant_care.pManagerViews.unitsManagementViews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.container.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class FetchingOccupiedUnitsStatus {
    INITIAL,
    FETCHING,
    SUCCESS,
    FAIL
}
data class OccupiedUnitsScreenUiState(
    val properties: List<PropertyUnit> = emptyList(),
    val fetchingOccupiedUnitsStatus: FetchingOccupiedUnitsStatus = FetchingOccupiedUnitsStatus.INITIAL,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val numOfRoomsSelected: String? = null,
    val unitName: String? = null,
    val tenant: String? = null,
    val roomNames: List<String> = emptyList()
)
class OccupiedUnitsScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(value = OccupiedUnitsScreenUiState())
    val uiState: StateFlow<OccupiedUnitsScreenUiState> = _uiState.asStateFlow()

    fun loadUserDetails() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserDetails->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails()
                    )
                }
            }
        }
    }

    fun fetchOccupiedProperties() {
        _uiState.update {
            it.copy(
                fetchingOccupiedUnitsStatus = FetchingOccupiedUnitsStatus.FETCHING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchAllOccupiedProperties(
                    tenantName = _uiState.value.tenant,
                    rooms = _uiState.value.numOfRoomsSelected,
                    roomName = _uiState.value.unitName
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            properties = response.body()?.data?.property!!,
                            fetchingOccupiedUnitsStatus = FetchingOccupiedUnitsStatus.SUCCESS
                        )
                    }
                    if(_uiState.value.roomNames.isEmpty()) {
                        fillRoomsList()
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            fetchingOccupiedUnitsStatus = FetchingOccupiedUnitsStatus.FAIL
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        fetchingOccupiedUnitsStatus = FetchingOccupiedUnitsStatus.FAIL
                    )
                }
            }
        }
    }

    fun fetchUnoccupiedProperties() {
        _uiState.update {
            it.copy(
                fetchingOccupiedUnitsStatus = FetchingOccupiedUnitsStatus.FETCHING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchUnoccupiedUnits()
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            properties = response.body()?.data?.property!!,
                            fetchingOccupiedUnitsStatus = FetchingOccupiedUnitsStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            fetchingOccupiedUnitsStatus = FetchingOccupiedUnitsStatus.FAIL
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        fetchingOccupiedUnitsStatus = FetchingOccupiedUnitsStatus.FAIL
                    )
                }
            }
        }
    }

    fun filterByNumberOfRooms(rooms: Int) {
        _uiState.update {
            it.copy(
                numOfRoomsSelected = rooms.toString()
            )
        }
        fetchOccupiedProperties()
    }

    fun filterByTenantName(tenant: String?) {
        _uiState.update {
            it.copy(
                tenant = tenant
            )
        }
        fetchOccupiedProperties()
    }

    fun filterByRoomName(unitName: String) {
        _uiState.update {
            it.copy(
                unitName = unitName
            )
        }
        fetchOccupiedProperties()
    }

    fun fillRoomsList() {
        val rooms = mutableListOf<String>()
        for(room in _uiState.value.properties) {
            rooms.add(room.propertyNumberOrName)
        }
        _uiState.update {
            it.copy(
                roomNames = rooms
            )
        }
    }

    fun unfilterProperties() {
        _uiState.update {
            it.copy(
                numOfRoomsSelected = null,
                tenant = null,
                unitName = null
            )
        }
        fetchOccupiedProperties()
    }

    init {
        loadUserDetails()
        fetchOccupiedProperties()

    }

}