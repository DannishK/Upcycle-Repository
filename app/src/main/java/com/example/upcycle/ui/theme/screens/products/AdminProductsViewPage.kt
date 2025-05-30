package com.example.upcycle.ui.theme.screens.products





import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.upcycle.data.EvaluationViewModel
import com.example.upcycle.models.ProductsModel
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.upcycle.R
import com.example.upcycle.data.authViewModel
import com.example.upcycle.navigation.ROUTE_ADMIN_PROFILE
import com.example.upcycle.navigation.ROUTE_POST_PRODUCT
import com.example.upcycle.navigation.ROUTE_USER_PROFILE


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AdminProductsViewPage(navController: NavController) {
    val authViewModel: authViewModel = viewModel()
    val context = LocalContext.current
    val productRepository = EvaluationViewModel()
    val selectedProduct = remember { mutableStateOf(ProductsModel()) }
    val productList = remember { mutableStateListOf<ProductsModel>() }

    // Call the function to fetch products (it will auto-update the list)
    LaunchedEffect(Unit) {
        productRepository.AdminViewProducts(
            product = selectedProduct,
            productList = productList,
            context = context
        )
    }

    val iconColor = Color(0xFF2E7D32)
    val textColor = Color(0xFF1B5E20)

    Box {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "No Image",
            contentScale = ContentScale.FillBounds
        )

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "UPCYCLE ADMINS",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUTE_POST_PRODUCT) }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add products")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(ROUTE_ADMIN_PROFILE) }) {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = "Account")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = iconColor,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )

            Text(
                text = "As an admin make sure you log out",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(8.dp),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productList) { product ->
                    ProductCard(product, navController)
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: ProductsModel, navController: NavController) {
    val productRepository = EvaluationViewModel()
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clickable {
                productRepository.AdminDeleteProduct(context, product.id, navController)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,  // Fixed this line
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Ksh ${product.price}", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold)
                Text(
                    text = product.description.take(40) + "...",
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
