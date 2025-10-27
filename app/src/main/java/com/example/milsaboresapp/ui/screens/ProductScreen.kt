// ui/screens/ProductsScreen.kt
package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.data.FakeRepository
import com.example.milsaboresapp.domain.model.Product
import com.example.milsaboresapp.ui.components.ProductCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    cartCount: Int,
    onProductClick: (Product) -> Unit   // 👈 esta firma debe matchear el NavGraph
) {
    var search by remember { mutableStateOf("") }
    val allProducts = FakeRepository.getAll()
    val filtered = allProducts.filter { it.name.contains(search, ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onOpenCart) {
                        BadgedBox(
                            badge = { if (cartCount > 0) Badge { Text(cartCount.toString()) } }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar producto") },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(8.dp)) {
                items(filtered) { product ->
                    ProductCard(
                        product = product,
                        onOpen = { onProductClick(product) } // 👈 coincide con la firma
                    )
                }
            }
        }
    }
}
