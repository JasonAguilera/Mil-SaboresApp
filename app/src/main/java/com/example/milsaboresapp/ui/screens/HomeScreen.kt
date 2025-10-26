package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.R
import com.example.milsaboresapp.data.FakeRepository
import com.example.milsaboresapp.ui.components.ImageCarousel
import com.example.milsaboresapp.ui.components.ProductCard
import kotlinx.coroutines.launch

private val BrownRed = Color(0xFF8D3A2E)
private val AppBarWarm = Color(0xFFFFB88A)
private val AppBarWarmText = Color(0xFF5A2E1B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenLogin: () -> Unit,
    onOpenProducts: () -> Unit,
    onOpenContacto: () -> Unit,
    onOpenNosotros: () -> Unit,
    onOpenCart: () -> Unit,
    cartCount: Int,
    onOpenProductDetail: (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val allProducts = remember { FakeRepository.getAll() }

    // Carrusel con solo 3 imágenes fijas
    val carouselImages = listOf(
        "android.resource://com.example.milsaboresapp/drawable/cheese",
        "android.resource://com.example.milsaboresapp/drawable/browni",
        "android.resource://com.example.milsaboresapp/drawable/croasan"
    )

    // ---- Filtros
    val categories = listOf("Todos", "pastelería", "tortas", "panadería")
    var selectedCat by remember { mutableStateOf("Todos") }

    val filtered = remember(selectedCat, allProducts) {
        if (selectedCat == "Todos") allProducts else allProducts.filter {
            it.category.equals(selectedCat, ignoreCase = true)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                NavigationDrawerItem(label = { Text("Productos") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }; onOpenProducts()
                })
                NavigationDrawerItem(label = { Text("Nosotros") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }; onOpenNosotros()
                })
                NavigationDrawerItem(label = { Text("Contacto") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }; onOpenContacto()
                })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mil Sabores", color = AppBarWarmText) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = AppBarWarmText)
                        }
                    },
                    actions = {
                        IconButton(onClick = onOpenLogin) {
                            Icon(Icons.Filled.Person, contentDescription = "Usuario", tint = AppBarWarmText)
                        }
                        IconButton(onClick = onOpenCart) {
                            BadgedBox(badge = {
                                if (cartCount > 0) Badge { Text(cartCount.toString()) }
                            }) {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito", tint = AppBarWarmText)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBarWarm.copy(alpha = 0.9f))
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                // Fondo
                Image(
                    painter = painterResource(R.drawable.fondo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp)
                ) {
                    Spacer(Modifier.height(12.dp))

                    // Logo centrado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "Logo Mil Sabores",
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // Carrusel (solo 3 imágenes, marrón rojizo)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = BrownRed),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        ImageCarousel(
                            items = carouselImages,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .padding(10.dp),
                            onItemClick = { onOpenProducts() },
                            showBadge = true,
                            badgeText = "DESTACADO"
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Filtros
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { cat ->
                            val selected = selectedCat == cat
                            FilterChip(
                                selected = selected,
                                onClick = { selectedCat = cat },
                                label = { Text(cat.replaceFirstChar { it.uppercase() }) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrownRed,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color(0xFFFFF0DE),
                                    labelColor = BrownRed
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Productos filtrados
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filtered) { product ->
                            ProductCard(
                                product = product,
                                quantity = 0,
                                onOpen = { onOpenProductDetail(product.id) }
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Botón ver más
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = onOpenProducts,
                            colors = ButtonDefaults.buttonColors(containerColor = BrownRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Ver todos los productos", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
