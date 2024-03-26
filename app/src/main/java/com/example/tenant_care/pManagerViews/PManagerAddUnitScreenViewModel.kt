package com.example.tenant_care.pManagerViews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PManagerAddUnitScreenUiState(
    val unitDetails: UnitDetails = UnitDetails(),
    val showSaveButton: Boolean = false
)

data class UnitDetails(
    val numOfRooms: Int = 0,
    val unitNameOrNumber: String = "",
    val unitDescription: String = "",
)

class PManagerAddUnitScreenViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(value = PManagerAddUnitScreenUiState())
    val uiState: StateFlow<PManagerAddUnitScreenUiState> = _uiState.asStateFlow()

    var unitDetails by mutableStateOf(UnitDetails())

    fun updateNumOfRooms(numOfRooms: Int) {
        unitDetails = unitDetails.copy(
            numOfRooms = numOfRooms
        )
        _uiState.update {
            it.copy(
                unitDetails = unitDetails,
                showSaveButton = checkIfAddUnitFieldsAreEmpty()
            )
        }
    }

    fun updateUnitNameOrNumber(unitNameOrNumber: String) {
        unitDetails = unitDetails.copy(
            unitNameOrNumber = unitNameOrNumber
        )
        _uiState.update {
            it.copy(
                unitDetails = unitDetails,
                showSaveButton = checkIfAddUnitFieldsAreEmpty()
            )
        }
    }

    fun updateUnitDescription(unitDescription: String) {
        unitDetails = unitDetails.copy(
            unitDescription = unitDescription
        )
        _uiState.update {
            it.copy(
                unitDetails = unitDetails,
                showSaveButton = checkIfAddUnitFieldsAreEmpty()
            )
        }
    }

    fun checkIfAddUnitFieldsAreEmpty(): Boolean {
        return unitDetails.numOfRooms != 0 && unitDetails.unitNameOrNumber.isNotEmpty() && unitDetails.unitDescription.isNotEmpty()
    }
}