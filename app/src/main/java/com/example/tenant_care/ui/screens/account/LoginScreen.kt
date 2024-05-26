package com.example.tenant_care.ui.screens.account

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.LoadingStatus

object LoginScreenDestination: AppNavigation {
    override val title: String = "Login screen"
    override val route: String = "login-screen"
    val phoneNumber: String = "phoneNumber"
    val password: String = "password"
    val routeWithArgs: String = "$route/{$phoneNumber}/{$password}"
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreenComposable(
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current
    BackHandler(onBack = {activity?.finish()})

    val viewModel: LoginScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    viewModel.checkIfAllFieldsAreFilled()

    var expanded by remember {
        mutableStateOf(false)
    }

    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
        viewModel.resetLoadingStatus()
    } else if(uiState.loadingStatus == LoadingStatus.FAILURE) {
        Toast.makeText(context, uiState.loginResponseMessage, Toast.LENGTH_SHORT).show()
        viewModel.resetLoadingStatus()
    }


    Box {
        LoginScreen(
            expanded = expanded,
            selectedOption = uiState.role.ifEmpty { "Click to select" },
            loginButtonEnabled = uiState.loginButtonEnabled,
            phoneNumber = uiState.phoneNumber,
            password = uiState.password,
            onDismissRequest = { expanded = false },
            onSelectOption = {
                viewModel.updateRole(it)
                viewModel.checkIfAllFieldsAreFilled()
                expanded = false
            },
            onDropDownButton = {
                expanded = true
            },
            onChangePhoneNumber = {
                viewModel.updatePhoneNumber(it)
                viewModel.checkIfAllFieldsAreFilled()
            },
            onChangePassword = {
                viewModel.updatePassword(it)
                viewModel.checkIfAllFieldsAreFilled()
            },
            onLogin = {
                if(uiState.role.lowercase() == "tenant") {
                    viewModel.loginAsTenant()
                } else if(uiState.role.lowercase() == "property manager") {
                    viewModel.loginAsPropertyManager()
                } else if(uiState.role.lowercase() == "caretaker") {
                    viewModel.loginAsCaretaker()
                }
            },
            exitApp = {
                activity?.finish()
            },
            loadingStatus = uiState.loadingStatus
        )
    }

}

@Composable
fun LoginScreen(
    expanded: Boolean,
    selectedOption: String,
    loginButtonEnabled: Boolean,
    phoneNumber: String,
    password: String,
    onDismissRequest: () -> Unit,
    onSelectOption: (option: String) -> Unit,
    onDropDownButton: () -> Unit,
    onChangePhoneNumber: (phoneNumber: String) -> Unit,
    onChangePassword: (password: String) -> Unit,
    onLogin: () -> Unit,
    exitApp: () -> Unit,
    loadingStatus: LoadingStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        IconButton(onClick = exitApp) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Exit app"
            )
        }
        Text(
            text = "Welcome to EstateEase",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            Text(
                text = "Login as: ",
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoleSelection(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            onSelectOption = onSelectOption,
            onDropDownButton = onDropDownButton,
            selectedOption = selectedOption
        )
        Spacer(modifier = Modifier.height(20.dp))
        InputField(
            value = phoneNumber,
            label = "Phone number",
            leadingIcon = R.drawable.phone,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Phone
            ),
            onChangeValue = onChangePhoneNumber,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        InputField(
            value = password,
            label = "Password",
            leadingIcon = R.drawable.baseline_password_24,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            onChangeValue = onChangePassword,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "Forgot password?")
        }
        Spacer(modifier = Modifier.weight(1f))
        LoginButton(
            enabled = loginButtonEnabled,
            onLogin = onLogin,
            loadingStatus = loadingStatus,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun RoleSelection(
    expanded: Boolean,
    selectedOption: String,
    onDismissRequest: () -> Unit,
    onDropDownButton: () -> Unit,
    onSelectOption: (option: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val roles: List<String> = listOf("Tenant", "Property manager", "Caretaker")
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .widthIn(150.dp)
            .clickable {
                onDropDownButton()
            }
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedOption,
                    modifier = Modifier
                        .padding(10.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Drop down menu"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest
            ) {
                if(expanded) {
                    roles.forEachIndexed { index, option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    modifier = Modifier
                                        .padding(10.dp)
                                )
                            },
                            onClick = {
                                onSelectOption(option)
                                onDismissRequest()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputField(
    value: String,
    label: String,
    leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    onChangeValue: (newValue: String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        shape = RoundedCornerShape(10.dp),
        label = {
            Text(text = label)
        },
        leadingIcon = {
            Icon(painter = painterResource(id = leadingIcon), contentDescription = null)
        },
        value = value,
        keyboardOptions = keyboardOptions,
        onValueChange = onChangeValue,
        modifier = modifier
    )
}

@Composable
fun LoginButton(
    enabled: Boolean,
    onLogin: () -> Unit,
    loadingStatus: LoadingStatus,
    modifier: Modifier = Modifier
) {
    Button(
        enabled = enabled && loadingStatus != LoadingStatus.LOADING,
        modifier = modifier,
        onClick = onLogin
    ) {
        if(loadingStatus == LoadingStatus.LOADING) {
            CircularProgressIndicator()
        } else {
            Text(text = "Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Tenant_careTheme {
        LoginScreen(
            expanded = false,
            selectedOption = "Click to select",
            loginButtonEnabled = false,
            phoneNumber = "",
            password = "",
            onDismissRequest = { /*TODO*/ },
            onSelectOption = {},
            onDropDownButton = { /*TODO*/ },
            onChangePhoneNumber = {},
            onChangePassword = {},
            onLogin = {},
            exitApp = {},
            loadingStatus = LoadingStatus.INITIAL
        )
    }
}