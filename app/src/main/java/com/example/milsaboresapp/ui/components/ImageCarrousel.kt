package com.example.milsaboresapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Carrusel a pantalla completa (sin mostrar imágenes adyacentes)
 */
@Composable
fun ImageCarousel(
    items: List<String>,
    modifier: Modifier = Modifier,
    autoScrollMs: Long = 4000,
    onItemClick: () -> Unit = {},
    showBadge: Boolean = false,
    badgeText: String = "DESTACADO"
) {
    if (items.isEmpty()) return

    var currentIndex by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    // Desplazamiento automático
    LaunchedEffect(items) {
        while (true) {
            delay(autoScrollMs)
            currentIndex = (currentIndex + 1) % items.size
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        // Imagen actual (a pantalla completa del carrusel)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onItemClick() },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box {
                AsyncImage(
                    model = items[currentIndex],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Etiqueta diagonal “DESTACADO”
                if (showBadge) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .rotate(-30f)
                            .background(Color(0xFFE53935), shape = MaterialTheme.shapes.small)
                            .padding(vertical = 4.dp, horizontal = 20.dp)
                    ) {
                        Text(
                            text = badgeText,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
