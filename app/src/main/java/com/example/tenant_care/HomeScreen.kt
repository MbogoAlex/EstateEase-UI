package com.example.tenant_care

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme

object HomeScreenDestination: AppNavigation {
    override val title: String = "Home Screen"
    override val route: String = "home-screen"

}
@Composable
fun HomeScreen(
    navigateToPManagerLoginScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "EstateEase",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Login As",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(100.dp))
        LoginButtonsDisplay(
            navigateToPManagerLoginScreen = navigateToPManagerLoginScreen
        )
    }
}

@Composable
fun LoginButtonsDisplay(
    navigateToPManagerLoginScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
//        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        LoginButton(
            buttonText = "TENANT",
            onClickButton = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(50.dp))
        LoginButton(
            buttonText = "CARETAKER",
            onClickButton = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(50.dp))
        LoginButton(
            buttonText = "PROPERTY MANAGER",
            onClickButton = { navigateToPManagerLoginScreen() }
        )
    }
}

@Composable
fun LoginButton(
    buttonText: String,
    onClickButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(52.dp)
            .fillMaxWidth(),
        onClick = { onClickButton() }
    ) {
        Text(
            text = buttonText,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginButtonPreview(
    modifier: Modifier = Modifier
) {
    Tenant_careTheme {
        LoginButton(
            buttonText = "TENANT",
            onClickButton = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(
    modifier: Modifier = Modifier
) {
    Tenant_careTheme {
        HomeScreen(
            navigateToPManagerLoginScreen = {}
        )
    }
}