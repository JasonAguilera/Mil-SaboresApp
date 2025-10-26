package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.data.FakeRepository
import com.example.milsaboresapp.domain.model.Product
import com.example.milsaboresapp.ui.components.ProductCard
import com.example.milsaboresapp.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    cartCount: Int,
    cartVM: CartViewModel,
    onProductClick: (Product) -> Unit
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onOpenCart) {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) Badge { Text(cartCount.toString()) }
                            }
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
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

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(filtered) { product ->
                    val quantity = cartVM.items.count { it.id == product.id }
                    ProductCard(
                        product = product,
                        quantity = quantity,                   // ← muestra badge de cantidad
                        onOpen = { onProductClick(product) }
                    )
                }
            }
        }
    }
}
