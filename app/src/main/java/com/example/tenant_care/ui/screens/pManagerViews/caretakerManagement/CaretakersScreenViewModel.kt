package com.example.tenant_care.ui.screens.pManagerViews.caretakerManagement

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.caretaker.CaretakerDT
import com.example.tenant_care.model.caretaker.CaretakerPaymentRequestBody
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.util.PaymentStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class CaretakersScreenUiState(
    val caretakers: List<CaretakerDT> = emptyList(),
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val month: String = "",
    val year: String = "",
    val paymentStatus: PaymentStatus = PaymentStatus.INITIAL
)
@RequiresApi(Build.VERSION_CODES.O)
class CaretakersScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = CaretakersScreenUiState())
    val uiState: StateFlow<CaretakersScreenUiState> = _uiState.asStateFlow()
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadStartupData() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect(){dsUserDetails->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails(),
                        month = LocalDateTime.now().month.toString(),
                        year = LocalDateTime.now().year.toString()
                    )
                }
            }
        }
        getCaretakers()
    }

    fun getCaretakers() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getActiveCaretakers()
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            caretakers = response.body()?.data?.caretaker!!
                        )
                    }
                }
            } catch (e: Exception) {

            }
        }
    }



    init {
        loadStartupData()
    }
}