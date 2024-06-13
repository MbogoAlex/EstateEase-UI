package com.example.tenant_care

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tenant_care.ui.screens.account.LoginScreenViewModel
import com.example.tenant_care.ui.screens.caretakerViews.CaretakerHomeScreenViewModel
import com.example.tenant_care.ui.screens.caretakerViews.meterReading.EditMeterReadingScreenViewModel
import com.example.tenant_care.ui.screens.caretakerViews.meterReading.NotUploadedScreenViewModel
import com.example.tenant_care.ui.screens.caretakerViews.meterReading.UploadedScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.PManagerHomeScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.PManagerLoginScreenViewModel
import com.example.tenant_care.ui.screens.generalViews.amenity.AmenityDetailsScreenViewModel
import com.example.tenant_care.ui.screens.generalViews.amenity.AmenityScreenViewModel
import com.example.tenant_care.ui.screens.generalViews.amenity.EditAmenityScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.caretakerManagement.AddCaretakerScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.caretakerManagement.CaretakerDetailsScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.caretakerManagement.CaretakersScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.rentPayment.AllTenantsPaymentsScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.rentPayment.RentPaymentsInfoHomeScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.rentPayment.RentPaymentsScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.rentPayment.SingleTenantPaymentDataScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.rentPayment.TenantsNotPaidScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.rentPayment.TenantsPaidScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.OccupiedUnitDetailsScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.OccupiedUnitsScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.PManagerAddUnitScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.UnitsManagementScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.UnoccupiedUnitDetailsScreenViewModel
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.UnoccupiedUnitsScreenViewModel
import com.example.tenant_care.ui.screens.tenantViews.TenantHomeScreenViewModel
import com.example.tenant_care.ui.screens.tenantViews.accountManagement.TenantLoginScreenViewModel
import com.example.tenant_care.ui.screens.tenantViews.rentPayment.PaymentHomeScreenViewModel
import com.example.tenant_care.ui.screens.tenantViews.rentPayment.PaymentsReportScreenViewModel
import com.example.tenant_care.ui.screens.tenantViews.rentPayment.RentInvoiceScreenViewModel
import com.example.tenant_care.ui.screens.caretakerViews.units.OccupiedUnitsScreenViewModel as OccupiedUnitsScreenViewModelCaretakerView
import com.example.tenant_care.ui.screens.caretakerViews.units.UnoccupiedUnitsScreenViewModel as UnoccupiedUnitsScreenViewModelCaretakerView

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
                apiRepository = apiRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        // initialize PManager Add new unit ViewModel

        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            PManagerAddUnitScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = this.createSavedStateHandle()
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

        // initialize TenantLoginScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            TenantLoginScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        // initialize PaymentHomeScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            PaymentHomeScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        // initialize RentInvoiceScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            val savedStateHandle = this.createSavedStateHandle()
            RentInvoiceScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = savedStateHandle
            )
        }

        // initialize PaymentsReportScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            PaymentsReportScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        // initialize TenantHomeScreenViewModel
        initializer {
            val dsRepository = estateEaseApplication().dsRepository
            TenantHomeScreenViewModel(
                dsRepository = dsRepository
            )
        }

        //initialize LoginScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            val savedStateHandle = this.createSavedStateHandle()
            LoginScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = savedStateHandle
            )
        }

        //initialize OccupiedUnitsScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            OccupiedUnitsScreenViewModelCaretakerView(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        //initialize UnoccupiedUnitsScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            UnoccupiedUnitsScreenViewModelCaretakerView(
                apiRepository =  apiRepository,
                dsRepository = dsRepository
            )
        }

        //initialize UploadedScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            UploadedScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        //initialize NotUploadedScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            NotUploadedScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        //initialize EditMeterReadingScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            EditMeterReadingScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        //initialize CaretakerHomeScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            CaretakerHomeScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        //initialize RentPaymentsInfoHomeScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            RentPaymentsInfoHomeScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        //initialize AmenityDetailsScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            AmenityDetailsScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        //initialize AmenityScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            AmenityScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
            )
        }

        //initialize EditAmenityScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            EditAmenityScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        //initialize CaretakersScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            CaretakersScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        //initialize AddCaretakerScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            AddCaretakerScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository
            )
        }

        //initialize CaretakerDetailsScreenViewModel
        initializer {
            val apiRepository = estateEaseApplication().container.apiRepository
            val dsRepository = estateEaseApplication().dsRepository
            CaretakerDetailsScreenViewModel(
                apiRepository = apiRepository,
                dsRepository = dsRepository,
                savedSavedStateHandle = this.createSavedStateHandle()
            )
        }
    }
}

fun CreationExtras.estateEaseApplication(): EstateEase =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EstateEase)