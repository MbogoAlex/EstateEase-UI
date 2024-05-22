package com.example.tenant_care.ui.screens.tenantViews

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun TenantAccountScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TenantAccountScreenTopPortion()
        Spacer(modifier = Modifier.height(50.dp))
        Column(
            modifier = modifier
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TenantAccountScreenBoxMenu(
                    menuText = "Edit Profile",
                    icon = painterResource(id = R.drawable.edit_profile_24)
                )
                Spacer(modifier = Modifier.weight(1f))
                TenantAccountScreenBoxMenu(
                    menuText = "Terms and conditions",
                    icon = painterResource(id = R.drawable.terms_and_conditions)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TenantAccountScreenBoxMenu(
                    menuText = "Privacy Policy",
                    icon = painterResource(id = R.drawable.privacy_policy)
                )
                Spacer(modifier = Modifier.weight(1f))
                TenantAccountScreenBoxMenu(
                    menuText = "Talk with Management",
                    icon = painterResource(id = R.drawable.support)
                )
            }
        }
    }
}

@Composable
fun TenantAccountScreenTopPortion(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 20.dp,
            bottomEnd = 20.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_tenant_care),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Agnes Jane",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Room Col C2",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tenant since: 07/22/2022",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TenantAccountScreenBoxMenu(
    menuText: String,
    icon: Painter,
    modifier: Modifier = Modifier
) {
    Card(
//        colors = CardDefaults.cardColors(
//            containerColor = Color.Blue
//        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .size(160.dp)
        ) {
            Text(
                text = menuText,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            androidx.compose.material3.Icon(
                painter = icon,
                contentDescription = menuText,
                modifier = Modifier
                    .background(Color.White)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TenantAccountScreenBoxMenuPreview() {
    Tenant_careTheme {
        TenantAccountScreenBoxMenu(
            menuText = "Edit Profile",
            icon = painterResource(id = R.drawable.edit_profile_24)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TenantAccountScreenPreview() {
    Tenant_careTheme {
        TenantAccountScreen()
    }
}