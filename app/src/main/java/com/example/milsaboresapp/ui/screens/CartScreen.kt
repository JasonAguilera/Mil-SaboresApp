package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.milsaboresapp.R
import com.example.milsaboresapp.viewmodel.CartLine
import com.example.milsaboresapp.viewmodel.CartViewModel

private val BrownRed = Color(0xFF8D3A2E)
private val AppBarWarm = Color(0xFFFFB88A)
private val AppBarWarmText = Color(0xFF5A2E1B)
private val AccentRed = Color(0xFFE53935)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartVM: CartViewModel,
    onBack: () -> Unit,
    onScanQr: () -> Unit   // botón QR visible en AppBar
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi carrito", color = AppBarWarmText) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = AppBarWarmText)
                    }
                },
                actions = {
                    // Botón QR visible arriba, bien notorio
                    FilledTonalButton(
                        onClick = onScanQr,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = AccentRed,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("QR -%")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBarWarm.copy(alpha = 0.95f))
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            // Fondo
            Image(
                painter = painterResource(R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (cartVM.items.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        ElevatedCard(
                            colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFF8E7))
                        ) {
                            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium, color = BrownRed)
                                Spacer(Modifier.height(8.dp))
                                Text("Agrega productos desde el Home o Productos.")
                            }
                        }
                    }
                } else {
                    // Lista
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        items(cartVM.items, key = { it.product.id }) { line ->
                            CartItemRow(
                                line = line,
                                onPlus = { cartVM.add(line.product, 1) },
                                onMinus = { cartVM.removeOne(line.product) },
                                onDelete = { cartVM.removeAll(line.product) }
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }

                    // Resumen con buen contraste
                    SummaryFooterCard(
                        subtotal = cartVM.totalBeforeDiscount(),
                        discountPct = cartVM.discountPct,
                        totalWithDiscount = cartVM.totalAfterDiscount(),
                        onClear = { cartVM.clear() },
                        onCheckout = { /* flujo de pago */ },
                        onScanQr = onScanQr
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    line: CartLine,
    onPlus: () -> Unit,
    onMinus: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E7)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {

            // Imagen del producto
            AsyncImage(
                model = line.product.imageUrl,
                contentDescription = line.product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.width(12.dp))

            // Info + contador
            Column(Modifier.weight(1f)) {
                Text(line.product.name, style = MaterialTheme.typography.titleMedium, color = BrownRed)
                Spacer(Modifier.height(2.dp))
                Text("${line.product.price} CLP", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF333333))
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Botón menos (pequeño)
                    IconButton(
                        onClick = onMinus,
                        modifier = Modifier
                            .size(28.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(Icons.Filled.Remove, contentDescription = "Restar", tint = BrownRed)
                    }

                    // Cantidad visible en una "pill"
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BrownRed.copy(alpha = 0.12f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${line.qty}",
                            color = BrownRed,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    // Botón más (pequeño)
                    IconButton(
                        onClick = onPlus,
                        modifier = Modifier
                            .size(28.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Sumar", tint = BrownRed)
                    }
                }
            }

            // Borrar
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFFFEBEE), CircleShape)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = AccentRed)
            }
        }
    }
}

@Composable
private fun SummaryFooterCard(
    subtotal: Int,
    discountPct: Int,
    totalWithDiscount: Int,
    onClear: () -> Unit,
    onCheckout: () -> Unit,
    onScanQr: () -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFFDF7)),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal", style = MaterialTheme.typography.titleMedium, color = Color(0xFF333333))
                Text("$subtotal CLP", style = MaterialTheme.typography.titleMedium, color = Color(0xFF333333))
            }

            if (discountPct > 0) {
                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Descuento ($discountPct%)", color = Color(0xFF2E7D32))
                    val saved = subtotal - totalWithDiscount
                    Text("-$saved CLP", color = Color(0xFF2E7D32))
                }
            }

            Spacer(Modifier.height(8.dp))
            Divider()
            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", style = MaterialTheme.typography.titleLarge, color = BrownRed)
                Text("$totalWithDiscount CLP", style = MaterialTheme.typography.titleLarge, color = BrownRed)
            }

            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onClear, modifier = Modifier.weight(1f)) {
                    Text("Vaciar")
                }
                // Repetimos QR aquí también, con alto contraste
                Button(
                    onClick = onScanQr,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                ) { Text("QR descuento", color = Color.White) }

                Button(
                    onClick = onCheckout,
                    modifier = Modifier.weight(1.3f),
                    colors = ButtonDefaults.buttonColors(containerColor = BrownRed)
                ) { Text("Pagar", color = Color.White) }
            }
        }
    }
}
