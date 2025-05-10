package com.example.upcycle

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.upcycle.ui.theme.UpcycleTheme
import com.example.upcycle.navigation.AppNavHost
import com.example.upcycle.navigation.ROUTE_ADMIN_HOME
import com.example.upcycle.navigation.ROUTE_LOGIN
import com.example.upcycle.navigation.ROUTE_USER_HOME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UpcycleTheme {
                MainScreen()

            }
        }
    }
}
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Loading State
    var isLoading by remember { mutableStateOf(true) }
    var initialRoute by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        // Call suspend function within coroutine scope
        val userType = getUserType(context)
        initialRoute = when (userType) {
            "Admin" -> ROUTE_ADMIN_HOME
            "User" -> ROUTE_USER_HOME
            else -> ROUTE_USER_HOME
        }
        isLoading = false
    }

    if (isLoading) {
        // Display a loading spinner while checking user type
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Initialize AppNavHost first
        AppNavHost(navController)

        // Navigate after AppNavHost is ready
        LaunchedEffect(initialRoute) {
            initialRoute?.let {
                navController.navigate(it) {
                    popUpTo(0) // Clear back stack
                }
            }
        }
    }
}

suspend fun getUserType(context: Context): String? {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        val userId = currentUser.uid

        // Firebase references
        val adminsRef = FirebaseDatabase.getInstance().getReference("Admins")
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")

        // Check if user is an Admin
        val isAdmin = adminsRef.child(userId).get().await().exists()
        if (isAdmin) return "Admin"

        // Check if user is a regular User
        val isUser = usersRef.child(userId).get().await().exists()
        if (isUser) return "User"
    }

    // If neither Admin nor User, return null
    return null
}