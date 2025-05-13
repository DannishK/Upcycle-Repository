package com.example.upcycle.ui.theme.screens.home



import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.upcycle.data.EvaluationViewModel
import com.example.upcycle.models.ProductsModel
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.upcycle.data.authViewModel
import com.example.upcycle.navigation.ROUTE_LOGIN
import com.example.upcycle.navigation.ROUTE_PRODUCT_DETAILS
import com.example.upcycle.R
import com.example.upcycle.navigation.ROUTE_ADD_PRODUCT
import com.example.upcycle.navigation.ROUTE_REGISTER
import com.example.upcycle.navigation.ROUTE_USER_HOME
import com.example.upcycle.navigation.ROUTE_USER_PROFILE




@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ViewModelConstructorInComposable")
@Composable

fun UserProductsViewPage(navController: NavController) {
    val authViewModel: authViewModel = viewModel()
    var expanded = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val productRepository = EvaluationViewModel()
    val selectedProduct = remember { mutableStateOf(ProductsModel()) }
    val productList = remember { mutableStateListOf<ProductsModel>() }
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE8F5E9), Color(0xFFDCEDC8))
    )
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

    val iconColor = Color(0xFF2E7D32)
    val textColor = Color(0xFF1B5E20)
    val buttonColor = Color(0xFF7B61FF)

    // Fetch products
    val products = productRepository.viewProducts(
        product = selectedProduct,
        productList = productList,
        context = context
    )




    Box(

    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            "No Image",
            contentScale = ContentScale.FillBounds,
            //modifier = Modifier.padding(innerPadding)
        )
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "UPCYCLE",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 35.sp

                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        expanded.value = !expanded.value // Toggle dropdown
                    }) {
                        Icon(imageVector = Icons.Filled.List, contentDescription = "Menu")
                    }
                    // Dropdown Menu
                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Submit your Item") },
                            onClick = {
                                expanded.value = false
                                navController.navigate(ROUTE_ADD_PRODUCT)
                            }
                        )
//                        DropdownMenuItem(
//                            text = { Text("Profile") },
//                            onClick = {
//                                expanded.value = false
//                                navController.navigate(ROUTE_USER_PROFILE)
//                            }
//                        )
                        DropdownMenuItem(
                            text = { Text("My Vision") },
                            onClick = {
                                expanded.value = false
                                navController.navigate(ROUTE_ADD_PRODUCT)
                            }
                        )
//                        DropdownMenuItem(
//                            text = { Text("Logout") },
//                            onClick = {
//                                expanded.value = false
//                                authViewModel.logout()
//                                navController.navigate(ROUTE_LOGIN) {
//                                    popUpTo(ROUTE_USER_HOME) { inclusive = true }
//                                }
//                            }
//                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(ROUTE_USER_PROFILE) }
                    ) {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = "Account")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldGreen,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White

                )
            )
            Text(
                text = "Explore our Refurbished Products",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                  //  .verticalScroll(rememberScrollState()), // Allow vertical scrolling
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductCard(product, navController)
                }
            }
        }
    }
}

    @Composable

    fun ProductCard(product: ProductsModel, navController: NavController) {
        val backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFE8F5E9), Color(0xFFDCEDC8))
        )
        val softPurple = Color(0xFFB8A9FF)
        val iconColor = Color(0xFF2E7D32)
        val textColor = Color(0xFF1B5E20)
        val buttonColor = Color(0xFF7B61FF)
        val MintGreen = Color(0xFFA5D6A7)
        val Charcoal = Color(0xFF212121)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable {
                    navController.navigate("$ROUTE_PRODUCT_DETAILS/${product.id}")
                }, colors = CardDefaults.cardColors(
               containerColor = MintGreen
               ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)

            ){
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )

                Column(modifier = Modifier.padding(8.dp),) {

                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = buttonColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally) // This centers just the text horizontally
                    )
                    Text(
                        text = "Ksh ${product.price}",
                        color = Color(0xFF388E3C),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally) // This also centers the price
                    )
                    Text(
                        text = product.description.take(40) + "...",
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Justify,
                        color = Charcoal,
                        modifier = Modifier.align(Alignment.CenterHorizontally)

                    )
                 }

            }
         }
        }
    }


