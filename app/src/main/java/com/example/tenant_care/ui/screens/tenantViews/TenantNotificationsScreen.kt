package com.example.tenant_care.ui.screens.tenantViews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun TenantsNotificationsScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Icon(
                painter = painterResource(id = R.drawable.notifications),
                contentDescription = "Notification Icon"
            )
            Text(
                text = "Notifications",
                fontSize = 25.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn() {
            items(10) {
                TenantNotificationItem()
            }
        }
    }
}

@Composable
fun TenantNotificationItem(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp
            )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = "Unit Assignment",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "19 minutes ago",
                style = TextStyle.Default.copy(
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TenantsNotificationScreenPreview(
    modifier: Modifier = Modifier
) {
    Tenant_careTheme {
        TenantsNotificationsScreen()
    }
}