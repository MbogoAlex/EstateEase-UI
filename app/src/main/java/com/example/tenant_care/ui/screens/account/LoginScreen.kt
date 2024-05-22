package com.example.tenant_care.ui.screens.account

import android.app.Activity
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun LoginScreenComposable(
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as? Activity)
    BackHandler(onBack = {activity?.finish()})
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedOption by remember {
        mutableStateOf("")
    }

    Box {
        LoginScreen(
            expanded = expanded,
            selectedOption = selectedOption.ifEmpty { "Click to select" },
            loginButtonEnabled = false,
            onDismissRequest = { expanded = false },
            onSelectOption = {
                selectedOption = it
                expanded = false
            },
            onDropDownButton = {
                expanded = true
            },
            onChangePhoneNumber = {},
            onChangePassword = {},
            onLogin = {},
            exitApp = {
                activity?.finish()
            }
        )
    }

}

@Composable
fun LoginScreen(
    expanded: Boolean,
    selectedOption: String,
    loginButtonEnabled: Boolean,
    onDismissRequest: () -> Unit,
    onSelectOption: (option: String) -> Unit,
    onDropDownButton: () -> Unit,
    onChangePhoneNumber: (phoneNumber: String) -> Unit,
    onChangePassword: (password: String) -> Unit,
    onLogin: () -> Unit,
    exitApp: () -> Unit,
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
            value = "",
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
            value = "",
            label = "Password",
            leadingIcon = R.drawable.phone,
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
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = onLogin
    ) {
        Text(text = "Login")
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
            onDismissRequest = { /*TODO*/ },
            onSelectOption = {},
            onDropDownButton = { /*TODO*/ },
            onChangePhoneNumber = {},
            onChangePassword = {},
            onLogin = {},
            exitApp = {}
        )
    }
}