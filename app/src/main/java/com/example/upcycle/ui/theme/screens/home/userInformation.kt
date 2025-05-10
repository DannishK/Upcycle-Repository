package com.example.upcycle.ui.theme.screens.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.upcycle.data.EvaluationViewModel
import com.example.upcycle.data.authViewModel
import com.example.upcycle.models.UserModel
import com.example.upcycle.navigation.ROUTE_LOGIN
import com.example.upcycle.navigation.ROUTE_USER_PRODUCTS
import com.example.upcycle.navigation.ROUTE_USER_PROFILE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.*

@Composable
fun UserProfileScreen(navController: NavController) {
    AuthGuard(navController) {
        val authViewModel: authViewModel = viewModel()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val context = LocalContext.current
        val userDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val userDetails = remember { mutableStateOf(UserModel()) }
        val loading = remember { mutableStateOf(true) }
        val iconColor = Color(0xFF2E7D32)
        val textColor = Color(0xFF1B5E20)
        val buttonColor = Color(0xFF7B61FF)

        // Fetch user data from Firebase
        LaunchedEffect(currentUser) {
            if (currentUser != null) {
                userDatabase.child(currentUser.uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(UserModel::class.java)
                            if (user != null) {
                                userDetails.value = user
                                loading.value = false
                            } else {
                                showToast(context, "User not found")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            showToast(context, "Failed to fetch user details: ${error.message}")
                        }
                    })
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (loading.value) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = "Welcome, ${userDetails.value.firstname}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email: ${userDetails.value.email}", color = textColor)
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    authViewModel.logout()//FirebaseAuth.getInstance().signOut()
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(ROUTE_USER_PROFILE) { inclusive = true }
                    }
                }) {
                    Text(text = "Logout")
                }
                Button(onClick = {

                    navController.navigate(ROUTE_USER_PRODUCTS)


                }) {
                    Text(text = "View your posted Products")
                }
            }
        }



    }

}
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
@Composable
fun AuthGuard(navController: NavController, content: @Composable () -> Unit) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser == null) {
        // Show a toast and navigate to the login screen
        Toast.makeText(LocalContext.current, "You are not logged in. Redirecting to login...", Toast.LENGTH_LONG).show()
        LaunchedEffect(Unit) {
            navController.navigate(ROUTE_LOGIN) {
                popUpTo(0) // Clear backstack so they can't go back to restricted areas
            }
        }
    } else {
        // If the user is logged in, render the actual content
        content()
    }
}

