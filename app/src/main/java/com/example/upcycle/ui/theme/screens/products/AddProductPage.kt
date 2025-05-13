package com.example.upcycle.ui.theme.screens.products


import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.upcycle.data.EvaluationViewModel
import com.example.upcycle.R
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import com.example.upcycle.navigation.ROUTE_USER_HOME

@Composable
fun PostProductScreen(navController: NavController) {
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imageUri.value = it }
    }

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val productViewModel: EvaluationViewModel = viewModel()

    val iconColor = Color(0xFF2E7D32)
    val textColor = Color(0xFF1B5E20)
    val buttonColor = Color(0xFF7B61FF)

    Box() {
        Image(
            painter = painterResource(id = R.drawable.background),
            "No Image",
            contentScale = ContentScale.FillBounds,
            //modifier = Modifier.padding(innerPadding)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier

                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Post An Evaluated Product",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = textColor

                )
            }

            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(10.dp)
                    .size(200.dp)
            ) {
                AsyncImage(
                    model = imageUri.value ?: R.drawable.placeholder_image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { launcher.launch("image/*") }
                )
            }

            Text(text = "Upload product image")

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name", fontWeight = FontWeight.Bold) },
                placeholder = { Text("Enter product name") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = textColor)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (Ksh)", fontWeight = FontWeight.Bold) },
                placeholder = { Text("Enter product price") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = textColor)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category", fontWeight = FontWeight.Bold) },
                placeholder = { Text("Enter category") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = textColor)
            )

            Spacer(modifier = Modifier.height(8.dp))

//            OutlinedTextField(
//                value = location,
//                onValueChange = { location = it },
//                label = { Text("Location", fontWeight = FontWeight.Bold) },
//                placeholder = { Text("Enter pickup/delivery location") },
//                shape = RoundedCornerShape(16.dp),
//                modifier = Modifier.fillMaxWidth(),
//                textStyle = TextStyle(color = textColor)
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description", fontWeight = FontWeight.Bold) },
                placeholder = { Text("Enter product details") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp, max = 300.dp)
                    .verticalScroll(scrollState),
                singleLine = false,
                textStyle = TextStyle(color = textColor)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = { navController.navigate(ROUTE_USER_HOME) },
                    colors = ButtonDefaults.buttonColors(containerColor = iconColor)
                ) {
                    Text("Dashboard", color = Color.White)
                }

                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        imageUri.value?.let {
                            productViewModel.AdminUploadProductWithImage(

                                uri = it,
                                context = context,
                                name = name,
                                price = price,
                                category = category,
                                location = location,
                                description = description,
                                navController = navController

                            )
                        } ?: Toast.makeText(context, "Pick an Image", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Add Product", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PostProductScreenPreview() {
    PostProductScreen(rememberNavController())
}