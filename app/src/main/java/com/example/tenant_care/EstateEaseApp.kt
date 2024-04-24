package com.example.tenant_care

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tenant_care.nav.NavigationGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EstateEaseApp(
    navController: NavHostController = rememberNavController()
) {
    NavigationGraph(navController = navController)
}