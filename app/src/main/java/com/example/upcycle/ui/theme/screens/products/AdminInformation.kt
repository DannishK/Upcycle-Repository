package com.example.upcycle.ui.theme.screens.products

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.upcycle.data.EvaluationViewModel
import com.example.upcycle.data.authViewModel
import com.example.upcycle.models.UserModel
import com.example.upcycle.navigation.ROUTE_LOGIN
import com.example.upcycle.navigation.ROUTE_USER_PRODUCTS
import com.example.upcycle.navigation.ROUTE_USER_PROFILE
import com.example.upcycle.ui.theme.screens.home.AuthGuard
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.*


@Composable
fun AdminProfileScreen(navController: NavController) {
    // Ensuring only authenticated users access this screen
    AuthGuard(navController) {
        val authViewModel: authViewModel = viewModel()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val context = LocalContext.current
        // Reference to the "Admins" node
        val adminDatabase = FirebaseDatabase.getInstance().getReference("Admins")
        val adminDetails = remember { mutableStateOf(UserModel()) }
        val loading = remember { mutableStateOf(true) }
        val iconColor = Color(0xFF2E7D32)
        val textColor = Color(0xFF1B5E20)
        val buttonColor = Color(0xFF7B61FF)

        // Fetch admin data from Firebase
        LaunchedEffect(currentUser) {
            if (currentUser != null) {
                adminDatabase.child(currentUser.uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val admin = snapshot.getValue(UserModel::class.java)
                            if (admin != null) {
                                adminDetails.value = admin
                                loading.value = false
                            } else {
                                showToast(context, "Admin not found")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            showToast(context, "Failed to fetch admin details: ${error.message}")
                        }
                    })
            }
        }

        // UI
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
                    text = "Welcome",//"Welcome, ${adminDetails.value.firstname}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email: ${adminDetails.value.email}", color = textColor)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        authViewModel.adminlogout()
                        navController.navigate(ROUTE_LOGIN) {
                            popUpTo(ROUTE_USER_PROFILE) { inclusive = true }
                        }
                    },
                    //colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(text = "Logout", color = Color.White)
                }




            }
        }
    }
}
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
