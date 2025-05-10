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
import com.example.upcycle.ui.theme.screens.evaluations.ProductsViewPage
import com.example.upcycle.ui.theme.screens.evaluations.UpdateProductScreen
import com.example.upcycle.ui.theme.screens.home.ProductDetailsScreen
import com.example.upcycle.ui.theme.screens.home.UserProductsViewPage
import com.example.upcycle.ui.theme.screens.home.UserProfileScreen
import com.example.upcycle.ui.theme.screens.login.AdminLoginScreen
import com.example.upcycle.ui.theme.screens.products.AdminProductsViewPage
import com.example.upcycle.ui.theme.screens.products.AdminProfileScreen
import com.example.upcycle.ui.theme.screens.products.PostProductScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost(navController: NavHostController= rememberNavController(),startDestination:String= ROUTE_SPLASH){
   // val currentUser = FirebaseAuth.getInstance().currentUser
    NavHost(navController=navController,startDestination=startDestination) {
        composable(ROUTE_SPLASH){ SplashScreen {
            navController.navigate(ROUTE_USER_HOME){
                popUpTo(ROUTE_SPLASH){inclusive=true}} }}
        composable(ROUTE_REGISTER) { RegisterScreen(navController) }
        composable(ROUTE_LOGIN){ LoginScreen(navController) }
        composable(ROUTE_ADMIN_LOGIN){ AdminLoginScreen(navController) }
        composable(ROUTE_USER_PRODUCTS){ ProductsViewPage(navController) }
        composable(ROUTE_ADMIN_HOME){ AdminProductsViewPage(navController) }
        composable(ROUTE_ADD_PRODUCT){ AddProductScreen(navController) }
        composable("$ROUTE_UPDATE_PRODUCT/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            if (productId != null) {
                UpdateProductScreen(navController, productId)
            }
        }

        composable(ROUTE_USER_HOME){ UserProductsViewPage(navController) }
        composable(ROUTE_ADMIN_LOGIN){ AdminLoginScreen(navController) }
        composable(ROUTE_POST_PRODUCT){ PostProductScreen(navController) }
        composable("$ROUTE_PRODUCT_DETAILS/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailsScreen(productId, navController)
        }
        composable(ROUTE_USER_PROFILE){ UserProfileScreen(navController) }
        composable(ROUTE_ADMIN_PROFILE){ AdminProfileScreen(navController) }




    }
}