package com.example.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ui.data.Screen

@Composable
fun BottomNavitgationBar(navHostController: NavHostController) {
    val items = listOf(
        Screen.Main,
        Screen.Recommend
    )

    NavigationBar {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem( selected = currentRoute == item.route,
                onClick = { navHostController.navigate(item.route) {
                    navHostController.graph.startDestinationRoute?.let {
                        popUpTo(it) {
                            saveState = true
//                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                } },

                icon = { Icon(
                    painter = painterResource(id = item.resId!!),
                    contentDescription = "Icon",
                    modifier = Modifier.size(20.dp))},

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.LightGray
                )
            )
        }
    }
}