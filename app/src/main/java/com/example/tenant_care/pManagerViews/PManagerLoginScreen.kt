package com.example.tenant_care.pManagerViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun PManagerLoginScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: PManagerLoginScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "BAck button"
                )
            }
            Text(
                text = "PropEase",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Image(
            painter = painterResource(id = R.drawable.pmanager_house_no_background),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
        )
        Text(
            text = "Property Manager Login",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))
        PManagerLoginInputField(
            label = "Phone number",
            value = uiState.pManagerLoginDetails.phoneNumber,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                viewModel.updatePhoneNumber(it)
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        PManagerLoginInputField(
            label = "Password",
            value = uiState.pManagerLoginDetails.password,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            onValueChange = {
                viewModel.updatePassword(it)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.Start)
        ) {
            Text(text = "Forgot password?")
        }
        Spacer(modifier = Modifier.height(30.dp))
        PManagerLoginButton(
            onLoginButtonClicked = { /*TODO*/ },
            buttonEnabled = uiState.showLoginButton,
            modifier = Modifier
                .widthIn(250.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PManagerLoginInputField(
    label: String,
    value: String,
    onValueChange: (value: String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        label = {
                Text(text = label)
        },
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = keyboardOptions,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun PManagerLoginButton(
    buttonEnabled: Boolean,
    onLoginButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        enabled = buttonEnabled,
        onClick = onLoginButtonClicked,
        modifier = modifier
    ) {
        Text(text = "Login")
    }
}

@Preview(showBackground = true)
@Composable
fun PManagerLoginInputFieldPreview() {
    Tenant_careTheme {
        PManagerLoginInputField(
            label = "Username",
            value = "",
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            onValueChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PManagerLoginScreenPreview() {
    Tenant_careTheme {
        PManagerLoginScreen()
    }
}