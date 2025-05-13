package com.example.upcycle.ui.theme.screens.home



import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.upcycle.R
import com.example.upcycle.models.ProductsModel
import com.google.firebase.database.*
import com.example.upcycle.data.initiateSTKPush
//import com.google.firebase.database.FirebaseDatabase


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailsScreen(
    productId: String,
    navController: NavController
) {
    val context = LocalContext.current
    val productDatabase = FirebaseDatabase.getInstance().getReference("userProducts")

    // Mutable states to hold product data
    val product = remember { mutableStateOf(ProductsModel()) }
    val isLoading = remember { mutableStateOf(true) }
    val iconColor = Color(0xFF2E7D32)
    val textColor = Color(0xFF1B5E20)
    val buttonColor = Color(0xFF7B61FF)

    // Fetch the product details from the database
    LaunchedEffect(productId) {
        productDatabase.child(productId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedProduct = snapshot.getValue(ProductsModel::class.java)
                if (fetchedProduct != null) {
                    product.value = fetchedProduct
                    isLoading.value = false
                } else {
                    Toast.makeText(context, "Product not found.", Toast.LENGTH_SHORT).show()
                    isLoading.value = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load product: ${error.message}", Toast.LENGTH_SHORT).show()
                isLoading.value = false
            }
        })
    }

    // UI
    if (isLoading.value) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = product.value.name) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) {
            Box(){
            Text(
                text = "UPCYCLE",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
                color = textColor

            )
                }
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "No Image",
                    contentScale = ContentScale.FillBounds,
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .align(Alignment.TopCenter),
                ) {

                    Spacer(modifier = Modifier.height(120.dp))
                    AsyncImage(
                        model = product.value.imageUrl,
                        contentDescription = product.value.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Name: ${product.value.name}",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Price: Ksh ${product.value.price}",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF388E3C),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Category: ${product.value.category}",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge
                    )
//                    Text(
//                        text = "Location: ${product.value.location}",
//                        textAlign = TextAlign.Center,
//                        color = Color.Black,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Description:",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = product.value.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Place BuyNowPopupButton here
                    BuyNowPopupButton(
                        selectedProduct = product.value,
                        onStkPush = { phone, amount, accountRef ->
                            initiateSTKPush(
                                context = context,
                                phoneNumber = phone,
                                amount = amount,
                                //accountRef = accountRef,
                                accountReference = "UPCYCLE",
                            )
                        }
                    )
                }
            }
        }
    }
}
