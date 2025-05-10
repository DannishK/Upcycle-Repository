package com.example.upcycle.ui.theme.screens.evaluations

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.upcycle.data.EvaluationViewModel
import com.example.upcycle.models.ProductsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.upcycle.R
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.upcycle.navigation.ROUTE_ADMIN_HOME
import com.example.upcycle.navigation.ROUTE_POST_PRODUCT
import com.example.upcycle.navigation.ROUTE_USER_HOME
import com.example.upcycle.ui.theme.screens.home.AuthGuard
import com.google.firebase.auth.FirebaseAuth


@Composable
fun UpdateProductScreen(navController: NavController, productId: String) {
    AuthGuard(navController) {
        val context = LocalContext.current
        val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let { imageUri.value = it }
            }

        var name by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("") }
        var location by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var imageUrl by remember { mutableStateOf("") }
        var showDialog by remember { mutableStateOf(false) }

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        val currentDataRef = FirebaseDatabase.getInstance()
            .getReference("productsModel/$productId")

        // Load current data
        DisposableEffect(Unit) {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val product = snapshot.getValue(ProductsModel::class.java)
                    product?.let {
                        name = it.name
                        price = it.price
                        category = it.category
                        location = it.location
                        description = it.description
                        imageUrl = it.imageUrl
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                }
            }
            currentDataRef.addValueEventListener(listener)
            onDispose { currentDataRef.removeEventListener(listener) }
        }

        val productViewModel: EvaluationViewModel = viewModel()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.padding(10.dp).align(Alignment.CenterStart),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(Color(0xFF7B61FF)),
                ) {

                    IconButton(onClick = { navController.navigate(ROUTE_ADMIN_HOME) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }


                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(16.dp)
            ) {
                Text(
                    text = "UPDATE PRODUCT",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(10.dp)
                    .size(200.dp)
            ) {
                AsyncImage(
                    model = imageUri.value ?: imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { launcher.launch("image/*") }
                )
            }

            Text(text = "Upload Picture Here")

            // Input Fields
            CustomTextField(value = name, label = "Product Name")
            CustomTextField(value = price, label = "Price (Ksh)")
            CustomTextField(value = category, label = "Category")
            CustomTextField(value = location, label = "Location")
            CustomTextField(value = description, label = "Description", isMultiline = true)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    productViewModel.deleteProduct(
                        context,
                        productId = productId,
                        navController = navController
                    )
                }) {
                    Text("DELETE")
                }

                Spacer(modifier = Modifier.width(20.dp))

                Button(onClick = { showDialog = true }) {
                    Text("UPDATE")
                }
            }
        }

        // Confirmation Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        imageUri.value?.let {
                            productViewModel.updateProduct(
                                context = context,
                                navController = navController,
                                name = name,
                                price = price,
                                category = category,
                                location = location,
                                description = description,
                                imageUrl = imageUrl,
                                productId = productId,
                                userId = userId.toString()
                            )

                        } ?: Toast.makeText(context, "Select an Image", Toast.LENGTH_LONG).show()
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text(text = "Confirm Update") },
                text = { Text("Are you sure you want to update this product?") }
            )
        }
    }
}

@Composable
fun CustomTextField(value: String, label: String, isMultiline: Boolean = false) {
    var text by remember { mutableStateOf(value) }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(label, fontWeight = FontWeight.Bold) },
        placeholder = { Text("Enter $label") },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .heightIn(min = if (isMultiline) 150.dp else 56.dp),
        textStyle = TextStyle(color = Color.Black),
        singleLine = !isMultiline
    )
}
