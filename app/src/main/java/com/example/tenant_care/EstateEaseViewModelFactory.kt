package com.example.tenant_care

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tenant_care.pManagerViews.PManagerHomeScreenViewModel
import com.example.tenant_care.pManagerViews.PManagerLoginScreenViewModel

object EstateEaseViewModelFactory {
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
    }
}

fun CreationExtras.estateEaseApplication(): EstateEase =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EstateEase)