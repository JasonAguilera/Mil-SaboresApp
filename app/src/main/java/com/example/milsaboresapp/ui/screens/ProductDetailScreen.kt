package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.milsaboresapp.R
import com.example.milsaboresapp.data.FakeRepository

private val BrownRed = Color(0xFF8D3A2E)
private val AppBarWarm = Color(0xFFFFB88A)
private val AppBarWarmText = Color(0xFF5A2E1B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    onAddToCart: (Int) -> Unit,
    onBack: () -> Unit,
    onGoProducts: () -> Unit
) {
    val product = FakeRepository.getAll().firstOrNull { it.id == productId }
    var qty by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "Detalle", color = AppBarWarmText) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = AppBarWarmText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBarWarm.copy(alpha = 0.9f))
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            // Fondo con imagen + velo/gradiente
            Image(
                painter = painterResource(R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0x80000000), Color(0x40000000), Color(0x00000000))
                        )
                    )
            )

            // Contenedor principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (product == null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Producto no encontrado", color = Color.White)
                    }
                    return@Column
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E7)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(230.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(product.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = BrownRed)
                        Text("${product.price} CLP", fontSize = 18.sp, color = AppBarWarmText)

                        Spacer(Modifier.height(8.dp))
                        Text(product.description, lineHeight = 20.sp)

                        Spacer(Modifier.height(16.dp))

                        // Selector de cantidad
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledIconButton(onClick = { if (qty > 1) qty-- }, shape = CircleShape) { Text("-") }
                            Text("$qty", modifier = Modifier.padding(horizontal = 16.dp), fontSize = 18.sp)
                            FilledIconButton(onClick = { qty++ }, shape = CircleShape) { Text("+") }
                        }

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onAddToCart(qty)
                                onGoProducts()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrownRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Agregar al carrito y seguir comprando", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
