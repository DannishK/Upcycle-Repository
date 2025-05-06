package com.example.upcycle.ui.theme.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.upcycle.data.authViewModel
import com.example.upcycle.navigation.ROUTE_LOGIN


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val authViewModel: authViewModel = viewModel()
    val DeepPurple = Color(0xFF5E35B1)
    val EmeraldGreen = Color(0xFF2E7D32)
    val SoftLilac = Color(0xFFD1C4E9)
    val MintGreen = Color(0xFFA5D6A7)
    val LightGrayCustom = Color(0xFFF5F5F5)
    val CoolGray = Color(0xFF757575)
    val Charcoal = Color(0xFF212121)
    val Amber = Color(0xFFFFB300)
    val primaryPurple = Color(0xFF7B61FF)
    val softPurple = Color(0xFFB8A9FF)
    val accentPink = Color(0xFFFF61A6)
    val lightBackground = Color(0xFFF5F5F5)
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE8F5E9), Color(0xFFDCEDC8))
    )
    val fieldBorderColor = Color(0xFF2E7D32)
    val iconColor = Color(0xFF2E7D32)
    val textColor = Color(0xFF1B5E20)
    val buttonColor = Color(0xFF7B61FF)
    val placeholderColor = Color(0xFF81C784)

    var firstname by remember { mutableStateOf("") }
    var secondname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    val context = LocalContext.current
    val passwordVisible by remember { mutableStateOf(false) }

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
            Text(
                text = "Sign Up",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = "Sign up an account",
                fontSize = 16.sp,
                letterSpacing = 2.sp,
                color = textColor.copy(alpha = 0.8f)
            )

            // Full name
            OutlinedTextField(
                value = firstname,
                onValueChange = { newFirstName -> firstname = newFirstName },
                label = null,
                placeholder = { Text("Full name", color = textColor) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = iconColor)
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


            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { newEmail -> email = newEmail },
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
                onValueChange = { newPassword -> password = newPassword },
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
                onClick = {authViewModel.signup(firstname, email, password, navController, context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                Text("Sign Up", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Sign In Redirect
            Row {
                Text("Already have an account? ", color = textColor)
                Text(
                    text = "Sign in",
                    color = buttonColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(ROUTE_LOGIN)
                    }
                )
            }
        }
    }}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {

        RegisterScreen(rememberNavController())

}