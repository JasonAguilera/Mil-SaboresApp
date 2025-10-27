package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.R
import com.example.milsaboresapp.data.FakeRepository
import com.example.milsaboresapp.data.UserDataStore
import com.example.milsaboresapp.ui.components.ProductCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val BrownRed = Color(0xFF8D3A2E)
private val WarmNav = Color(0xFFFFB88A)
private val WarmText = Color(0xFF5A2E1B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenLogin: () -> Unit,
    onOpenProducts: () -> Unit,
    onOpenContacto: () -> Unit,
    onOpenNosotros: () -> Unit,
    onOpenCart: () -> Unit,
    onLoggedOut: () -> Unit,
    onOpenProductDetail: (String) -> Unit, // para tocar tarjetas de abajo
    cartCount: Int
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val userDS = remember { UserDataStore(ctx) }

    // 3 imágenes fijas del carrusel
    val carouselImages = remember {
        listOf(R.drawable.cheese, R.drawable.torta, R.drawable.pavlova)
    }
    val pagerState = rememberPagerState(pageCount = { carouselImages.size })

    // Auto-advance cíclico cada 3s
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val next = (pagerState.currentPage + 1) % carouselImages.size
            pagerState.animateScrollToPage(next)
        }
    }

    // ---- Filtro de categorías ----
    val categories = listOf("Todos", "Pastelería", "Panadería", "Tortas")
    var selectedCategory by remember { mutableStateOf("Todos") }

    val allProducts = remember { FakeRepository.getAll() }
    val filteredForGrid = remember(selectedCategory, allProducts) {
        if (selectedCategory == "Todos") allProducts
        else allProducts.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }
    // mantenemos 6 ítems como tenías
    val products = remember(filteredForGrid) { filteredForGrid.take(6) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFFFFF3E0),
                drawerTonalElevation = 8.dp
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    "Mil Sabores 🍰",
                    style = MaterialTheme.typography.headlineSmall,
                    color = BrownRed,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(Modifier.height(16.dp))

                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Productos") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onOpenProducts()
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Nosotros") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onOpenNosotros()
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Contacto") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onOpenContacto()
                    }
                )
                Spacer(Modifier.height(24.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión", color = Color(0xFFD32F2F)) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            userDS.clear()
                            drawerState.close()
                            onLoggedOut()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mil Sabores", color = WarmText) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = WarmText)
                        }
                    },
                    actions = {
                        IconButton(onClick = onOpenCart) {
                            BadgedBox(
                                badge = {
                                    if (cartCount > 0) Badge { Text(cartCount.toString()) }
                                }
                            ) {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito", tint = WarmText)
                            }
                        }
                        IconButton(onClick = onOpenLogin) {
                            Icon(Icons.Filled.Person, contentDescription = "Usuario", tint = WarmText)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmNav.copy(alpha = 0.95f))
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Box(Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.fondo),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                Column(
                    Modifier
                        .padding(padding)
                        .padding(horizontal = 12.dp)
                        .fillMaxSize()
                ) {
                    Spacer(Modifier.height(16.dp))

                    // Logo principal
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Logo Mil Sabores",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(120.dp)
                    )

                    Spacer(Modifier.height(14.dp))

                    // ==== CARRUSEL con fondo marrón rojizo y clic navega a Productos ====
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BrownRed),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .height(180.dp)
                                .clickable { onOpenProducts() } // al tocar, navega a productos
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                    elevation = CardDefaults.cardElevation(0.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Image(
                                        painter = painterResource(id = carouselImages[page]),
                                        contentDescription = "Destacado ${page + 1}",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }

                            // Badge "DESTACADO" (estilo cinta)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .offset(x = (-6).dp, y = 12.dp)
                                    .clip(MaterialTheme.shapes.extraSmall)
                                    .background(Color(0xFFE53935))
                            ) {
                                Text(
                                    "DESTACADO",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // ====== FILTROS DE CATEGORÍA (nuevo) ======
                    Spacer(Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color(0xFFFFE0B2),              // fondo cuando no está seleccionado
                                    labelColor = Color(0xFF5A2E1B),                  // texto normal
                                    selectedContainerColor = Color(0xFF8D3A2E),      // fondo cuando está seleccionado
                                    selectedLabelColor = Color.White,                // texto cuando está seleccionado
                                    selectedLeadingIconColor = Color.White           // si usas ícono, su color
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Recomendados para ti",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(products.size) { index ->
                            val product = products[index]
                            ProductCard(
                                product = product,
                                quantity = 0,
                                onOpen = { onOpenProductDetail(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
