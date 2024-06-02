package com.example.ui.data

import com.example.ui.R

sealed class Screen(val route: String, val resId: Int? = null) {
    object Splash : Screen("SplashScreen")
    object Main : Screen("MainScreen", R.drawable.main)
    object Recommend : Screen("RecommendScreen", R.drawable.recommend)
    object Furniture : Screen("Furniture")
    object Appliances : Screen("Appliances")
    object Plants : Screen("Plants")
    object Decoration : Screen("Decoration")
    object Kitchenware : Screen("Kitchenware")
    object Search : Screen("Search")
}
