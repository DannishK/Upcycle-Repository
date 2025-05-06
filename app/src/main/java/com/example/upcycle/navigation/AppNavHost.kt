package com.example.upcycle.navigation

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.upcycle.ui.theme.screens.login.LoginScreen
import com.example.upcycle.ui.theme.screens.register.RegisterScreen
import com.example.upcycle.ui.theme.screens.SplashScreen
import com.example.upcycle.ui.theme.screens.evaluations.AddProductScreen
import com.example.upcycle.ui.theme.screens.evaluations.UpdateProductScreen
import com.example.upcycle.ui.theme.screens.home.ProductsViewPage

@Composable
fun AppNavHost(navController: NavHostController= rememberNavController(),startDestination:String= ROUTE_SPLASH){
    NavHost(navController=navController,startDestination=startDestination) {
        composable(ROUTE_SPLASH){ SplashScreen {
            navController.navigate(ROUTE_REGISTER){
                popUpTo(ROUTE_SPLASH){inclusive=true}} }}
        composable(ROUTE_REGISTER) { RegisterScreen(navController) }
        composable(ROUTE_LOGIN){ LoginScreen(navController) }
        composable(ROUTE_HOME){ ProductsViewPage(navController) }
        composable(ROUTE_EVALUATION){ AddProductScreen(navController) }
        composable(ROUTE_UPDATE_PRODUCT){ UpdateProductScreen(navController, productId = String()) }


    }
}