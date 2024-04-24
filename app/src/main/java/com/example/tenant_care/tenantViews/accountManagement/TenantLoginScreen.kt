package com.example.tenant_care.tenantViews.accountManagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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

object TenantLoginScreenDestination: AppNavigation {
    override val title: String = "Tenant Login Screen"
    override val route: String = "tenant-login-screen"

}
@Composable
fun TenantLoginScreen(
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
        TenantLoginBody()
    }
}

@Composable
fun TenantLoginBody(
    modifier: Modifier = Modifier
) {
    Column(
//        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        TenantLoginInputFieldsDisplay()
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
        TenantLoginButton(onLoginButtonClicked = { /*TODO*/ })
    }
}

@Composable
fun TenantLoginInputFieldsDisplay(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TenantLoginInputField(
            label = "Room Number",
            onValueChanged = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(40.dp))
        TenantLoginInputField(
            label = "Phone Number",
            onValueChanged = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(40.dp))
        TenantLoginInputField(
            label = "Password",
            onValueChanged = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun TenantLoginButton(
    onLoginButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(52.dp)
            .fillMaxWidth(),
        onClick = { onLoginButtonClicked() }
    ) {
        Text(
            text = "LOGIN",
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantLoginInputField(
    label: String,
    onValueChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        label = {
                Text(
                    text = label
                )
        },
        onValueChange = {},
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
            onValueChanged = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TenantLoginScreenPreview() {
    Tenant_careTheme {
        TenantLoginScreen()
    }
}