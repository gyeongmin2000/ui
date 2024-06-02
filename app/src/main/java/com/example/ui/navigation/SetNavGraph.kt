package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ui.data.Product
import com.example.ui.data.ProductList
import com.example.ui.data.Screen
import com.example.ui.screen.CreateProjectScreen
import com.example.ui.screen.MainScreen
import com.example.ui.screen.RecommendScreen
import com.example.ui.screen.SplashScreen
import com.example.ui.view.ProductGrid

@Composable
fun SetNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Main.route) {
            MainScreen()
        }


        composable(Screen.Recommend.route) { RecommendScreen(navController) }

        //composable("createProject") { CreateProjectScreen(navController) }
        composable("createProject?projectId={projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId")
            CreateProjectScreen(navController = navController, projectId = if (projectId == -1L) null else projectId)
        }
    }
}

@Composable
fun SetProductListNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Furniture.route) {

        composable(Screen.Furniture.route) {
            ProductGrid(
                item = ProductList.filter { it.type == "Furniture" },
                navController = navController
            )
        }

        composable(Screen.Appliances.route) {
            ProductGrid(
                item = ProductList.filter { it.type == "Appliance" },
                navController = navController
            )
        }

        composable(Screen.Plants.route) {
            ProductGrid(
                item = ProductList.filter { it.type == "Plant" },
                navController = navController
            )
        }

        composable(Screen.Decoration.route) {
            ProductGrid(
                item = ProductList.filter { it.type == "Decoration" },
                navController = navController
            )
        }
        composable(Screen.Kitchenware.route) {
            ProductGrid(
                item = ProductList.filter { it.type == "Kitchenware" },
                navController = navController
            )
        }

        composable(Screen.Search.route) {
            val searchList = remember {
                navController.previousBackStackEntry?.savedStateHandle?.get<List<Product>>("searchlist")
            }
            if (searchList != null) {
                ProductGrid(
                    item = searchList,
                    navController = navController
                )
            }
        }

    }
}