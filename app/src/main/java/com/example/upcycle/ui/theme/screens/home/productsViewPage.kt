package com.example.upcycle.ui.theme.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.upcycle.data.EvaluationViewModel
import com.example.upcycle.models.ProductsModel
import com.example.upcycle.navigation.ROUTE_UPDATE_PRODUCT

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun ViewProducts(navController: NavHostController) {
    val context = LocalContext.current
    val productRepository = EvaluationViewModel()

    val selectedProduct = remember {
        mutableStateOf(ProductsModel("", "", "", "", ""))
    }
    val productList = remember {
        mutableStateListOf<ProductsModel>()
    }

    val products = productRepository.viewProducts(
        selectedProduct, productList, context
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "All Products",
            fontSize = 30.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(products) {
                ProductItem(
                    name = "it.name",
                    price = "it.price",
                    category = "it.category",
                    location = "it.location",
                    description = "it.description",
                    imageUrl = "it.imageUrl",
                    productId = "it.id",
                    navController = navController,
                    productRepository = productRepository
                )
            }
        }
    }
}

@Composable
fun ProductItem(
    name: String,
    price: String,
    category: String,
    location: String,
    description: String,
    imageUrl: String//List<String>,
    productId: String,
    navController: NavHostController,
    productRepository: EvaluationViewModel
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .height(230.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = Color.Gray)
        ) {
            Row {
                Column {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(200.dp)
                            .height(150.dp)
                            .padding(10.dp)
                    )

                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(
                            onClick = {
                                productRepository.deleteProduct(context, productId, navController)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Text("REMOVE", color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                navController.navigate("$ROUTE_UPDATE_PRODUCT/$productId")
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color.Green)
                        ) {
                            Text("UPDATE", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    InfoLabel("Product Name", name)
                    InfoLabel("Price", "Ksh $price")
                    InfoLabel("Category", category)
                    InfoLabel("Location", location)
                    InfoLabel("Description", description)
                }
            }
        }
    }
}

@Composable
fun InfoLabel(label: String, value: String) {
    Text(label, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
}

@Preview
@Composable
fun ViewProductsPreview() {
    ViewProducts(rememberNavController())
}
