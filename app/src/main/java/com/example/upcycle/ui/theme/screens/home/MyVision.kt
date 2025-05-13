package com.example.upcycle.ui.theme.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyVisionScreen() {
    val backgroundColor = Color(0xFFf1f8e9)  // Soft green background
    val textColor = Color(0xFF388E3C)       // Darker green for text
    val EmeraldGreen = Color(0xFF2E7D32)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Vision",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                backgroundColor = EmeraldGreen,
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation if needed */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = """
                    At Upcycle, our vision is to revolutionize the way electronic waste is handled by creating a sustainable online marketplace for company-refurbished electronics. 
                    
                    Our goal is to extend the life cycle of electronic devices, reduce environmental impact, and make high-quality technology more accessible and affordable for everyone. 
                    
                    By connecting consumers to certified refurbished products, we aim to minimize electronic waste while promoting a culture of responsible consumption. Together, we can build a greener, more sustainable future.
                """.trimIndent(),
                fontSize = 18.sp,
                color = textColor,
                textAlign = TextAlign.Justify
            )
        }
    }
}
