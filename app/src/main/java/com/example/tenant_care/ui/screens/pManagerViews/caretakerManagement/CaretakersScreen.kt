package com.example.tenant_care.ui.screens.pManagerViews.caretakerManagement

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.model.caretaker.CaretakerDT
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.caretakersExample

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CaretakersScreenComposable(
    navigateToCaretakerDetailsScreen: (caretakerId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: CaretakersScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier) {
        CaretakersScreen(
            caretakers = uiState.caretakers,
            navigateToCaretakerDetailsScreen = navigateToCaretakerDetailsScreen
        )
    }
}

@Composable
fun CaretakersScreen(
    caretakers: List<CaretakerDT>,
    navigateToCaretakerDetailsScreen: (caretakerId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Caretakers",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(caretakers) {
                CareTakerCell(
                    caretakerDT = it,
                    navigateToCaretakerDetailsScreen = navigateToCaretakerDetailsScreen,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun CareTakerCell(
    caretakerDT: CaretakerDT,
    navigateToCaretakerDetailsScreen: (caretakerId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = "Caretaker",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = caretakerDT.fullName
                )
            }
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = caretakerDT.phoneNumber
                )
            }
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = caretakerDT.email
                )
            }
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navigateToCaretakerDetailsScreen(caretakerDT.caretakerId.toString()) },
            ) {
                Text("More")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CaretakersScreenPreview() {
    Tenant_careTheme {
        CaretakersScreen(
            caretakers = caretakersExample,
            navigateToCaretakerDetailsScreen = {}
        )
    }
}