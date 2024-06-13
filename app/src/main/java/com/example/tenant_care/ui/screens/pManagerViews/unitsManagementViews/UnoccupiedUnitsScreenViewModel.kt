package com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class FetchingUnOccupiedUnitsStatus {
    INITIAL,
    FETCHING,
    SUCCESS,
    FAIL
}
data class UnOccupiedUnitsScreenUiState(
    val properties: List<PropertyUnit> = emptyList(),
    val fetchingUnOccupiedUnitsStatus: FetchingUnOccupiedUnitsStatus = FetchingUnOccupiedUnitsStatus.INITIAL,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val numOfRoomsSelected: String? = null,
    val unitName: String? = null,
    val roomNames: List<String> = emptyList()
)
class UnoccupiedUnitsScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(value = UnOccupiedUnitsScreenUiState())
    val uiState: StateFlow<UnOccupiedUnitsScreenUiState> = _uiState.asStateFlow()

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

    fun fetchUnOccupiedProperties() {
        _uiState.update {
            it.copy(

                fetchingUnOccupiedUnitsStatus = FetchingUnOccupiedUnitsStatus.FETCHING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchFilteredProperties(
                    tenantName = null,
                    rooms = _uiState.value.numOfRoomsSelected,
                    roomName = _uiState.value.unitName,
                    occupied = false
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            properties = response.body()?.data?.property!!.reversed(),
                            fetchingUnOccupiedUnitsStatus = FetchingUnOccupiedUnitsStatus.SUCCESS
                        )
                    }
                    fillRoomsList()
                } else {
                    _uiState.update {
                        it.copy(
                            fetchingUnOccupiedUnitsStatus = FetchingUnOccupiedUnitsStatus.FAIL
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        fetchingUnOccupiedUnitsStatus = FetchingUnOccupiedUnitsStatus.FAIL
                    )
                }
            }
        }
    }



    fun filterByNumberOfRooms(rooms: String) {
        _uiState.update {
            it.copy(
                numOfRoomsSelected = rooms.toString()
            )
        }
        fetchUnOccupiedProperties()
    }


    fun filterByRoomName(unitName: String) {
        _uiState.update {
            it.copy(
                unitName = unitName
            )
        }
        fetchUnOccupiedProperties()
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
                unitName = null
            )
        }
        fetchUnOccupiedProperties()
    }


    init {
        loadUserDetails()
        fetchUnOccupiedProperties()

    }
}