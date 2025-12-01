package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.R

private val BrownRed = Color(0xFF8D3A2E)
private val SoftCream = Color(0xFFFFF8E7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    orderId: Long,
    total: Int,
    onBackHome: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comprobante de compra") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Fondo
            Image(
                painter = painterResource(R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            // Contenido
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = SoftCream.copy(alpha = 0.95f),
                    tonalElevation = 6.dp,
                    shadowElevation = 6.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Mil Sabores",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = BrownRed
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "Boleta electrónica",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(16.dp))

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = BrownRed,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "¡Tu pedido ha sido recibido!",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(16.dp))

                        // Datos principales de la boleta
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("N° de pedido:", fontWeight = FontWeight.SemiBold)
                            Text("#$orderId")
                        }

                        Spacer(Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total pagado:", fontWeight = FontWeight.SemiBold)
                            Text("$total CLP", color = BrownRed)
                        }

                        Spacer(Modifier.height(12.dp))

                        Divider()

                        Spacer(Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Te hemos enviado una copia de la boleta\na tu correo registrado.",
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "Gracias por preferir Mil Sabores 💖",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onBackHome,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al inicio")
                }
            }
        }
    }
}
