package com.example.tenant_care

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tenant_care.pManagerViews.PManagerAddUnitScreenViewModel
import com.example.tenant_care.pManagerViews.PManagerHomeScreenViewModel
import com.example.tenant_care.pManagerViews.PManagerLoginScreenViewModel
import com.example.tenant_care.pManagerViews.rentPayment.AllTenantsPaymentsScreenViewModel
import com.example.tenant_care.pManagerViews.rentPayment.RentPaymentsScreenViewModel
import com.example.tenant_care.pManagerViews.rentPayment.SingleTenantPaymentDataScreenViewModel
import com.example.tenant_care.pManagerViews.rentPayment.TenantsNotPaidScreenViewModel
import com.example.tenant_care.pManagerViews.rentPayment.TenantsPaidScreenViewModel
import com.example.tenant_care.pManagerViews.unitsManagementViews.OccupiedUnitDetailsScreenViewModel
import com.example.tenant_care.pManagerViews.unitsManagementViews.OccupiedUnitsScreenViewModel
import com.example.tenant_care.pManagerViews.unitsManagementViews.UnitsManagementScreenViewModel
import com.example.tenant_care.pManagerViews.unitsManagementViews.UnoccupiedUnitDetailsScreenViewModel
import com.example.tenant_care.pManagerViews.unitsManagementViews.UnoccupiedUnitsScreenViewModel

object EstateEaseViewModelFactory {
    @RequiresApi(Build.VERSION_CODES.O)
    val Factory = viewModelFactory {
        // initialize PManagerLoginViewModel

        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            PManagerLoginScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        // initialize SplashScreen ViewModel

        initializer {
            val dsRepository = estateEaseApplication().dsRepository
            SplashScreenViewModel(
                dsRepository = dsRepository
            )
        }

        // initialize PManagerHomeScreen ViewModel

        initializer {
            val dsRepository = estateEaseApplication().dsRepository
            val apiRepository = estateEaseApplication().container.apiRepository
            PManagerHomeScreenViewModel(
                dsRepository = dsRepository,
                apiRepository = apiRepository
            )
        }

        // initialize PManager Add new unit ViewModel

        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            PManagerAddUnitScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        // initialize UnitsManagementScreen ViewModel

        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            val savedStateHandle = this.createSavedStateHandle()
            UnitsManagementScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = savedStateHandle
            )
        }

        // initialize OccupiedUnitsScreen ViewModel

        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            OccupiedUnitsScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        // initialize UnoccupiedUnitsScreen ViewModel

        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            UnoccupiedUnitsScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        // initialize UnoccupiedUnitScreenDetails ViewModel

        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            val savedStateHandle = this.createSavedStateHandle()
            UnoccupiedUnitDetailsScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = savedStateHandle
            )
        }

        // initialize OccupiedUnitDetailsScreen ViewModel

        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            val savedStateHandle = this.createSavedStateHandle()
            OccupiedUnitDetailsScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = savedStateHandle
            )
        }

        // initialize RentPaymentsScreenViewModel

        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            val savedStateHandle = this.createSavedStateHandle()
            RentPaymentsScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = savedStateHandle
            )

        }

        // initialize TenantsPaidScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            TenantsPaidScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        // initialize TenantsNotPaidScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            TenantsNotPaidScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        // initialize TenantsNotPaidScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            AllTenantsPaymentsScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        // initialize SingleTenantPaymentScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            val savedStateHandle = this.createSavedStateHandle()
            SingleTenantPaymentDataScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = savedStateHandle
            )
        }
    }
}

fun CreationExtras.estateEaseApplication(): EstateEase =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EstateEase)