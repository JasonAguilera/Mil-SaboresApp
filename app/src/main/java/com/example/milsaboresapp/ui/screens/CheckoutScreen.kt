package com.example.milsaboresapp.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.R
import com.example.milsaboresapp.data.CheckoutRepository
import com.example.milsaboresapp.data.OrderRemote
import com.example.milsaboresapp.data.UserDataStore
import com.example.milsaboresapp.viewmodel.CartViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartVM: CartViewModel,
    onBack: () -> Unit,
    onOrderSuccess: (OrderRemote) -> Unit
) {
    val scope = rememberCoroutineScope()
    val repo = remember { CheckoutRepository() }

    val ctx = LocalContext.current
    val userDS = remember { UserDataStore(ctx) }

    // DataStore (nombre / correo / login)
    val storedName by userDS.userName.collectAsState(initial = "")
    val storedEmail by userDS.userEmail.collectAsState(initial = "")
    val isLoggedIn by userDS.isLoggedIn.collectAsState(initial = false)
    // Si luego quieres token:
    // val jwtToken by userDS.userToken.collectAsState(initial = "")

    // ========= ESTADOS DE CONTACTO =========
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Autocompletar cuando haya sesión
    LaunchedEffect(isLoggedIn, storedName, storedEmail) {
        if (isLoggedIn) {
            if (storedName.isNotBlank()) fullName = storedName
            if (storedEmail.isNotBlank()) email = storedEmail
        }
    }

    // ========= ESTADOS DE TARJETA (simulada) =========
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    // ========= ESTADOS DE UBICACIÓN / MAPS =========
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(ctx)
    }

    var userLat by remember { mutableStateOf<Double?>(null) }
    var userLng by remember { mutableStateOf<Double?>(null) }
    var locationStatus by remember { mutableStateOf("Sin ubicación seleccionada") }

    // Lanzador de permiso de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { loc ->
                    if (loc != null) {
                        userLat = loc.latitude
                        userLng = loc.longitude
                        locationStatus =
                            "Ubicación lista para delivery (${String.format("%.4f", loc.latitude)}, ${String.format("%.4f", loc.longitude)})"
                    } else {
                        locationStatus = "No se pudo obtener la ubicación actual."
                    }
                }
                .addOnFailureListener {
                    locationStatus = "Error al obtener ubicación."
                }
        } else {
            locationStatus = "Permiso de ubicación denegado."
        }
    }

    // ========= ESTADOS GENERALES =========
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val subtotal = cartVM.totalBeforeDiscount()
    val discountPct = cartVM.discountPct
    val total = cartVM.totalAfterDiscount()
    val discountAmount = subtotal - total

    val canConfirm =
        cartVM.items.isNotEmpty() &&
                fullName.isNotBlank() &&
                email.isNotBlank() &&
                phone.isNotBlank() &&
                cardNumber.length >= 12 &&
                cardHolder.isNotBlank() &&
                expiry.isNotBlank() &&
                cvv.length >= 3

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar pedido") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { padding ->

        // ===== FONDO + CONTENEDOR =====
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Fondo desenfocado suave
            Image(
                painter = painterResource(R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.20f
            )

            // Card central
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier
                        .padding(18.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    // ===== CONTENIDO SUPERIOR =====
                    Column {

                        // --- Resumen ---
                        Text("Resumen de compra", style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(8.dp))

                        Text("Subtotal: $subtotal CLP")
                        Text("Descuento: -$discountAmount CLP ($discountPct%)")
                        Text(
                            "Total a pagar: $total CLP",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(Modifier.height(20.dp))

                        // --- Datos de contacto ---
                        Text("Datos de contacto", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = {
                                Text(
                                    if (isLoggedIn) "Nombre (tomado de tu cuenta)"
                                    else "Nombre completo"
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = {
                                Text(
                                    if (isLoggedIn) "Correo (tomado de tu cuenta)"
                                    else "Correo electrónico"
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Teléfono de contacto") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(20.dp))

                        // --- Ubicación / Delivery ---
                        Text("Ubicación de entrega (simulada)", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))

                        Button(
                            onClick = {
                                // Pedimos permiso de ubicación fina
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Usar mi ubicación actual")
                        }

                        Spacer(Modifier.height(6.dp))
                        Text(
                            locationStatus,
                            style = MaterialTheme.typography.bodySmall
                        )

                        // Si tenemos coordenadas, mostramos botón para abrir Google Maps
                        val lat = userLat
                        val lng = userLng
                        if (lat != null && lng != null) {
                            Spacer(Modifier.height(4.dp))
                            TextButton(
                                onClick = {
                                    val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=$lat,$lng(Entrega Mil Sabores)")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                    mapIntent.setPackage("com.google.android.apps.maps")
                                    ctx.startActivity(mapIntent)
                                }
                            ) {
                                Text("Ver ubicación en Google Maps")
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        // --- Datos tarjeta (simulación) ---
                        Text("Datos de pago (simulación)", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { cardNumber = it.filter { c -> c.isDigit() }.take(16) },
                            label = { Text("Número de tarjeta") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = cardHolder,
                            onValueChange = { cardHolder = it },
                            label = { Text("Nombre en la tarjeta") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = expiry,
                                onValueChange = { expiry = it.take(5) },
                                label = { Text("MM/AA") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = cvv,
                                onValueChange = { cvv = it.filter { c -> c.isDigit() }.take(4) },
                                label = { Text("CVV") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Simulación visual — no se realiza ningún cobro real.",
                            style = MaterialTheme.typography.bodySmall
                        )

                        if (errorMsg != null) {
                            Spacer(Modifier.height(8.dp))
                            Text(errorMsg ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    }

                    // ===== BOTÓN CONFIRMAR =====
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                errorMsg = null
                                try {
                                    val order = repo.checkoutCart(
                                        cartLines = cartVM.items.toList(),
                                        fullName = fullName,
                                        email = email,
                                        phone = phone
                                        // Si quisieras, también podrías mandar lat/lng al back
                                    )

                                    cartVM.clear()
                                    onOrderSuccess(order)

                                } catch (e: Exception) {
                                    errorMsg = "Error al procesar el pedido"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        enabled = canConfirm && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Confirmar pedido")
                        }
                    }
                }
            }
        }
    }
}
