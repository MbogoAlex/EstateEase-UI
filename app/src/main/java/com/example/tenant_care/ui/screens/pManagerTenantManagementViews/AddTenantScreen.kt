package com.example.tenant_care.ui.screens.pManagerTenantManagementViews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun AddTenantScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        AddTenantForm()
    }
}

@Composable
fun AddTenantForm(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            PManagerAddTenantInputField(
                label = "Full Name",
                value = "",
                maxLines = 1,
                onValueChange = {},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            PManagerAddTenantInputField(
                label = "National ID / Passport",
                value = "",
                maxLines = 1,
                onValueChange = {},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            PManagerAddTenantInputField(
                label = "Phone Number",
                value = "",
                maxLines = 1,
                onValueChange = {},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            PManagerAddTenantInputField(
                label = "Email",
                value = "",
                maxLines = 1,
                onValueChange = {},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Room allocation:",
                fontWeight = FontWeight.Bold
            )
            Row {

            }
        }
    }
}

@Composable
fun numberOfRoomsSelection(
    viewModel: AddTenantScreenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    var items = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Column {
            TextButton(onClick = { expanded = true }) {
                if (uiState.roomDetails.numOfRooms == 0) {
                    Text(text = "No. of rooms")
                } else {
                    Text(text = "${uiState.roomDetails.numOfRooms} rooms")
                }

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                for (item in items) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "$item rooms")
                        },
                        onClick = {
                            viewModel.updateNumOfRooms(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PManagerAddTenantInputField(
    label: String,
    value: String,
    maxLines: Int,
    onValueChange: (value: String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        label = {
            Text(text = label)
        },
        maxLines = maxLines,
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

@Preview(showBackground = true)
@Composable
fun AddTenantScreenPreview() {
    Tenant_careTheme {
        AddTenantScreen()
    }
}