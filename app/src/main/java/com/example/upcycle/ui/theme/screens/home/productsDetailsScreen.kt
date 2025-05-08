package com.example.upcycle.ui.theme.screens.home



import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.upcycle.R
import com.example.upcycle.data.EvaluationViewModel
import com.example.upcycle.models.ProductsModel
import com.google.firebase.database.*



@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailsScreen(navController: NavController, productId: String) {
    val context = LocalContext.current
    val productRepository = EvaluationViewModel()
    val productState = remember { mutableStateOf<ProductsModel?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    // Reference to the database
    val databaseRef = FirebaseDatabase.getInstance().getReference("userProducts/$productId")

    // Fetch product details from Firebase
    LaunchedEffect(Unit) {
        databaseRef.get().addOnSuccessListener { snapshot ->
            val product = snapshot.getValue(ProductsModel::class.java)
            productState.value = product
            isLoading.value = false
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to load product details", Toast.LENGTH_SHORT).show()
            isLoading.value = false
        }
    }

    if (isLoading.value) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        val product = productState.value
        product?.let {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Product Details") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF328EAD),
                            navigationIconContentColor = Color.White,
                            titleContentColor = Color.White,
                            actionIconContentColor = Color.White

                        )
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = product.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Price: Ksh ${product.price}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF388E3C)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Location: ${product.location}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Category: ${product.category}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Description",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = product.description,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            Toast.makeText(context, "Purchase initiated for ${product.name}", Toast.LENGTH_SHORT).show()
                            // You can implement purchase logic here
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B61FF)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "BUY NOW", color = Color.White)
                    }
                }
            }
        }
    }
}
