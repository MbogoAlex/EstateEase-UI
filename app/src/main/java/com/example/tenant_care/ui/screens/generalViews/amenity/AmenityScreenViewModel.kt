package com.example.tenant_care.ui.screens.generalViews.amenity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.amenity.Amenity
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.util.ExecutionStatus
import com.example.tenant_care.util.LoadingStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AmenityScreenUiState(
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val amenities: List<Amenity> = emptyList(),
    val searchText: String? = null,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL,
)
class AmenityScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(value = AmenityScreenUiState())
    val uiState: StateFlow<AmenityScreenUiState> = _uiState.asStateFlow()

    fun loadStartupData() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserDetails->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails()
                    )
                }
            }
        }
        fetchAmenities()
    }

    fun filterAmenities(searchText: String) {
        _uiState.update {
            it.copy(
                searchText = searchText
            )
        }
        fetchAmenities()
    }

    fun fetchAmenities() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchFilteredAmenities(uiState.value.searchText)
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            amenities = response.body()?.data?.amenities!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAILURE
                        )
                    }
                    Log.e("FETCH_AMENITIES_ERROR_RESPONSE", response.toString())
                }
            }catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAILURE
                    )
                }
                Log.e("FETCH_AMENITIES_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    fun unfilter() {
        _uiState.update {
            it.copy(
                searchText = null
            )
        }
        fetchAmenities()
    }

    init {
        loadStartupData()
    }
}