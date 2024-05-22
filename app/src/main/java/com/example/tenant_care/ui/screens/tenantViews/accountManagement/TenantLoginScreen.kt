package com.example.tenant_care.ui.screens.tenantViews.accountManagement

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme

object TenantLoginScreenDestination: AppNavigation {
    override val title: String = "Tenant Login Screen"
    override val route: String = "tenant-login-screen"
    val phoneNumber: String = "phoneNumber"
    val password: String = "password"
    val routeWithArgs: String = "$route/{$phoneNumber}/{$password}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TenantLoginScreenComposable(
    navigateToTenantHomeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: TenantLoginScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    viewModel.checkIfAllFieldsFilled()

    if(uiState.loginStatus == LoginStatus.SUCCESS) {
        Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show()
        navigateToTenantHomeScreen()
        viewModel.resetLoginStatus()
    } else if(uiState.loginStatus == LoginStatus.FAILURE) {
        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
        viewModel.resetLoginStatus()
    }

    Box {
        TenantLoginScreen(
            roomValue = uiState.roomName,
            phoneNumberValue = uiState.phoneNumber,
            onChangePhoneNumberValue = {
                viewModel.updatePhoneNumber(it)
            },
            passwordValue = uiState.password,
            enabled = uiState.buttonEnabled,
            onChangePasswordValue = {
                viewModel.updatePassword(it)
            },
            onLoginButtonClicked = {
                viewModel.loginTenant()
            },
            loginStatus = uiState.loginStatus
        )
    }
}
@Composable
fun TenantLoginScreen(
    roomValue: String,
    phoneNumberValue: String,
    onChangePhoneNumberValue: (newValue: String) -> Unit,
    passwordValue: String,
    onChangePasswordValue: (newValue: String) -> Unit,
    onLoginButtonClicked: () -> Unit,
    enabled: Boolean,
    loginStatus: LoginStatus,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "EstateEase",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(70.dp))
        TenantLoginBody(
            roomValue = roomValue,
            phoneNumberValue = phoneNumberValue,
            onChangePhoneNumberValue = onChangePhoneNumberValue,
            passwordValue = passwordValue,
            onChangePasswordValue = onChangePasswordValue,
            enabled = enabled,
            onLoginButtonClicked = onLoginButtonClicked,
            loginStatus = loginStatus
        )
    }
}

@Composable
fun TenantLoginBody(
    roomValue: String,
    phoneNumberValue: String,
    onChangePhoneNumberValue: (newValue: String) -> Unit,
    passwordValue: String,
    onChangePasswordValue: (newValue: String) -> Unit,
    onLoginButtonClicked: () -> Unit,
    enabled: Boolean,
    loginStatus: LoginStatus,
    modifier: Modifier = Modifier
) {
    Column(
//        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        TenantLoginInputFieldsDisplay(
            roomValue = roomValue,
            phoneNumberValue = phoneNumberValue,
            onChangePhoneNumberValue = onChangePhoneNumberValue,
            passwordValue = passwordValue,
            onChangePasswordValue = onChangePasswordValue
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Forgot password?",
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        TenantLoginButton(
            enabled = enabled,
            onLoginButtonClicked = onLoginButtonClicked,
            loginStatus = loginStatus
        )
    }
}

@Composable
fun TenantLoginInputFieldsDisplay(
    roomValue: String,
    phoneNumberValue: String,
    onChangePhoneNumberValue: (newValue: String) -> Unit,
    passwordValue: String,
    onChangePasswordValue: (newValue: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        TenantLoginInputField(
            value = phoneNumberValue,
            label = "Phone Number",
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Phone
            ),
            onValueChanged = {
                onChangePhoneNumberValue(it)
            }
        )
        Spacer(modifier = Modifier.height(40.dp))
        TenantLoginInputField(
            value = passwordValue,
            label = "Password",
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            onValueChanged = {
                onChangePasswordValue(it)
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun TenantLoginButton(
    onLoginButtonClicked: () -> Unit,
    enabled: Boolean,
    loginStatus: LoginStatus,
    modifier: Modifier = Modifier
) {
    Button(
        enabled = enabled && loginStatus != LoginStatus.LOADING,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(52.dp)
            .fillMaxWidth(),
        onClick = { onLoginButtonClicked() }
    ) {
        if(loginStatus == LoginStatus.LOADING) {
            CircularProgressIndicator()
        } else {
            Text(
                text = "LOGIN",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantLoginInputField(
    label: String,
    value: String,
    keyboardOptions: KeyboardOptions,
    onValueChanged: (newValue: String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        label = {
                Text(
                    text = label
                )
        },
        onValueChange = onValueChanged,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.LightGray
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun TenantLoginInputFieldPreview() {
    Tenant_careTheme {
        TenantLoginInputField(
            label = "National ID / Passport",
            value = "",
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            onValueChanged = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TenantLoginScreenPreview() {
    Tenant_careTheme {
        TenantLoginScreen(
            roomValue = "",
            phoneNumberValue = "",
            onChangePhoneNumberValue = {},
            passwordValue = "",
            onChangePasswordValue = {},
            enabled = false,
            onLoginButtonClicked = {},
            loginStatus = LoginStatus.INITIAL
        )
    }
}