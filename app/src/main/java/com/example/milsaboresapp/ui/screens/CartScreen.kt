package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.R
import com.example.milsaboresapp.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartVM: CartViewModel, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi carrito", color = Color(0xFF5A2E1B)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color(0xFF5A2E1B))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFB88A))
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(Modifier.padding(padding)) {
            Image(
                painter = painterResource(R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (cartVM.items.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Tu carrito está vacío", color = Color.White)
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(cartVM.items) { p ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E7))
                            ) {
                                Column(Modifier.padding(10.dp)) {
                                    Text(p.name, color = Color(0xFF8D3A2E))
                                    Text("$${p.price}")
                                    Button(
                                        onClick = { cartVM.remove(p) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D3A2E)),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Text("Eliminar", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                    Column {
                        Text(
                            "Total: $${cartVM.total()}",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Button(
                            onClick = { cartVM.clear() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D3A2E))
                        ) {
                            Text("Finalizar compra", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
