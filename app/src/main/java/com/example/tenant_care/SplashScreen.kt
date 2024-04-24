package com.example.tenant_care

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme
import kotlinx.coroutines.delay

object SplashScreenDestination: AppNavigation {
    override val title: String = "Splash screen"
    override val route: String = "splash-screen"

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SplashScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToPManagerHomeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: SplashScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()





    when(uiState.initializingStatus) {
        InitializingStatus.INITIAL -> {}
        InitializingStatus.LOADING -> {}
        InitializingStatus.SUCCESS -> {
            viewModel.resetInitializationStatus()
            if(uiState.userDSDetails.userId != null) {
                if(uiState.userDSDetails.roleId == 1) {
                    navigateToPManagerHomeScreen()

                }
            } else {
                Log.i("INITIALIZATION_STATUS", uiState.initializingStatus.name)
                navigateToHomeScreen()
            }
        }
        InitializingStatus.FAIL -> {}
    }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.pmanager_house_no_background),
            contentDescription = "Splash screen"
        )
        Text(
            text = "EstateEase",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    Tenant_careTheme {
        SplashScreen(
            navigateToHomeScreen = {},
            navigateToPManagerHomeScreen = {}
        )
    }
}