package com.example.upcycle.ui.theme.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.upcycle.data.authViewModel
import com.example.upcycle.navigation.ROUTE_ADD_PRODUCT
import com.example.upcycle.navigation.ROUTE_ADMIN_LOGIN
import com.example.upcycle.navigation.ROUTE_POST_PRODUCT
import com.example.upcycle.navigation.ROUTE_REGISTER
import com.example.upcycle.navigation.ROUTE_USER_HOME

@Composable
fun LoginScreen(navController: NavController) {
    val authViewModel: authViewModel = viewModel()
//    val DeepPurple = Color(0xFF5E35B1)
//    val EmeraldGreen = Color(0xFF2E7D32)
//    val SoftLilac = Color(0xFFD1C4E9)
//    val MintGreen = Color(0xFFA5D6A7)
//    val LightGrayCustom = Color(0xFFF5F5F5)
//    val CoolGray = Color(0xFF757575)
//    val Charcoal = Color(0xFF212121)
//    val Amber = Color(0xFFFFB300)
//    val primaryPurple = Color(0xFF7B61FF)
//    val softPurple = Color(0xFFB8A9FF)
//    val accentPink = Color(0xFFFF61A6)
//    val lightBackground = Color(0xFFF5F5F5)
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE8F5E9), Color(0xFFDCEDC8))
    )
    //val fieldBorderColor = Color(0xFF2E7D32)
    val iconColor = Color(0xFF2E7D32)
    val textColor = Color(0xFF1B5E20)
    val buttonColor = Color(0xFF7B61FF)
   // val placeholderColor = Color(0xFF81C784)

   // var firstname by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    //var password2 by remember { mutableStateOf("") }
    val context = LocalContext.current
   // val passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
//            Card (modifier = Modifier.padding(10.dp).align(Alignment.CenterStart),
//                shape = RoundedCornerShape(20.dp),
//                elevation = CardDefaults.cardElevation(10.dp),
//                colors = CardDefaults.cardColors(Color(0xFF7B61FF)),) {
//
//                IconButton(onClick = { navController.navigate(ROUTE_USER_HOME) }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back"
//                    )
//                }
//
//
//            }
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterEnd),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(Color(0xFF7B61FF))
                ) {
                    IconButton(onClick = { navController.navigate(ROUTE_ADMIN_LOGIN) }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Admin Login",
                            tint = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(140.dp))

            Text(
                text = "Sign In",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = "Sign In to an account",
                fontSize = 16.sp,
                letterSpacing = 2.sp,
                color = textColor.copy(alpha = 0.8f)
            )




            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email Address", color = textColor) },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = iconColor)
                },
                textStyle = TextStyle(
                    color = textColor,
                    fontFamily = FontFamily.SansSerif
                ),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedBorderColor = fieldBorderColor,
//                    unfocusedBorderColor = fieldBorderColor,
//                    textColor = textColor
//                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = textColor) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = iconColor)
                },
                textStyle = TextStyle(
                    color = textColor,
                    fontFamily = FontFamily.SansSerif
                ),
//                visualTransformation = PasswordVisualTransformation(),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedBorderColor = fieldBorderColor,
//                    unfocusedBorderColor = fieldBorderColor,
//                    textColor = textColor
//                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Sign Up Button
            Button(
                onClick = { authViewModel.login(email,password, navController, context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                Text("Sign In", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Sign In Redirect
            Row {
                Text("Don't have an account? ", color = textColor)
                Text(
                    text = "Sign Up",
                    color = buttonColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(ROUTE_REGISTER)
                    }
                )
            }
        }
    }}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}