package com.example.upcycle.ui.theme.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.upcycle.data.EvaluationViewModel
import com.example.upcycle.data.initiateSTKPush
import com.example.upcycle.models.ProductsModel

@Composable

fun BuyNowPopupButton(
    selectedProduct: ProductsModel,
    onStkPush: (phone: String, amount: String, accountRef: String) -> Unit
) {

    val context = LocalContext.current
    val phoneNumber = remember { mutableStateOf("") }
    val isDialogOpen = remember { mutableStateOf(false) }

    Button(
        onClick = { isDialogOpen.value = true },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(Color(0xFF388E3C))
    ) {
        Text(text = "BUY NOW", color = Color.White)
    }

    if (isDialogOpen.value) {
        AlertDialog(
            onDismissRequest = { isDialogOpen.value = false },
            title = { Text("Pay Ksh ${selectedProduct.price}") },
            text = {
                Column {
                    Text("You are about to pay Ksh ${selectedProduct.price} for “${selectedProduct.name}”")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phoneNumber.value,
                        onValueChange = { phoneNumber.value = it },
                        label = { Text("Phone Number") },
                        placeholder = { Text("07XXXXXXXX") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Call the STK Push function
                        onStkPush(
                            phoneNumber.value,
                            selectedProduct.price.toString(),
                            selectedProduct.id ?: ""
                        )
                        isDialogOpen.value = false
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF388E3C))
                ) {
                    Text("Pay Now")
                }
            },
            dismissButton = {
                Button(onClick = { isDialogOpen.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
