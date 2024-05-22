package com.example.tenant_care.ui.screens.tenantViews.amenity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun AmenityComposable(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        AmenityScreen()
    }
}

@Composable
fun AmenityScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        SearchAmenityField()
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(10) {
                AmenityItemCell()
            }
        }
    }
}

@Composable
fun AmenityItemCell(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(
                    text = "Gas exchange",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Provider: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "John Mwangi")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Contact: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "07745673")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "More about this amenity"
                )
            }
        }
    }
}

@Composable
fun SearchAmenityField(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        label = {
                Text(text = "Search amenity")
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.business),
                contentDescription = null
            )
        },
        onValueChange = {},
        keyboardActions = KeyboardActions(),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Go,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable fun AmenityScreenPreview() {
    Tenant_careTheme {
        AmenityScreen()
    }
}