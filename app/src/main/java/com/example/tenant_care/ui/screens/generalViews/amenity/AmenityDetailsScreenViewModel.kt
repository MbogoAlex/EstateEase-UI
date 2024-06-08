package com.example.tenant_care.ui.screens.generalViews.amenity

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.amenity.Amenity
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.util.ExecutionStatus
import com.example.tenant_care.util.LoadingStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import com.example.tenant_care.util.sampleAmenity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AmenityDetailsScreenUiState(
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val amenity: Amenity = sampleAmenity,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL,
    val executionStatus: ExecutionStatus = ExecutionStatus.INITIAL
)
class AmenityDetailsScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = AmenityDetailsScreenUiState())
    val uiState: StateFlow<AmenityDetailsScreenUiState> = _uiState.asStateFlow()

    private val amenityId: String? = savedStateHandle[AmenityDetailsScreenDestination.amenityId]
    fun loadStartupData() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserModel->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserModel.toUserDetails()
                    )
                }
            }
        }
        getAmenity()
    }

    fun getAmenity() {
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchAmenity(amenityId!!.toInt())
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            amenity = response.body()?.data?.amenity!!
                        )
                    }
                } else {
                    Log.e("FETCH_AMENITY_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                Log.e("FETCH_AMENITY_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    fun deleteAmenity(id: Int) {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.deleteAmenity(id)
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            executionStatus = ExecutionStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            executionStatus = ExecutionStatus.FAILURE
                        )
                    }
                    Log.e("DELETE_AMENITY_FAILURE_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        executionStatus = ExecutionStatus.FAILURE
                    )
                }
                Log.e("DELETE_AMENITY_FAILURE_EXCEPTION", e.toString())
            }
        }
    }

    fun resetExecutionStatus() {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.INITIAL
            )
        }
    }

    init {
        loadStartupData()
    }
}