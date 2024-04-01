package com.example.tenant_care

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tenant_care.nav.NavigationGraph

@Composable
fun EstateEaseApp(
    navController: NavHostController = rememberNavController()
) {
    NavigationGraph(navController = navController)
}