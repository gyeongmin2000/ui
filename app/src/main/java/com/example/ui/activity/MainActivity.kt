package com.example.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager.LayoutParams.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.data.Screen
import com.example.ui.navigation.BottomNavitgationBar
import com.example.ui.navigation.SetNavGraph
import com.example.ui.ui.theme.UiTheme


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    //bottomBar 표시 여부
                    var isVisible = true
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    navBackStackEntry?.destination?.route?.let { route ->
                        isVisible = when(route) {
                            Screen.Splash.route -> false
                            else -> true
                        }
                    }

                    Scaffold(
                        bottomBar = { if(isVisible) {
                            BottomNavitgationBar(navHostController = navController)
                        }}
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            SetNavGraph(navController = navController)
                        }
                    }
                }
            }
        }
        window.apply {
            WindowCompat.setDecorFitsSystemWindows(this, false)
            setFlags( FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS )
        }
    }
}


