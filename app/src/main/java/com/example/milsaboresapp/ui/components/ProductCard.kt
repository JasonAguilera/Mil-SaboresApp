package com.example.milsaboresapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.milsaboresapp.domain.model.Product

@Composable
fun ProductCard(
    product: Product,
    quantity: Int = 0,                 // ← cantidad en carrito
    onOpen: () -> Unit = {}
) {
    val pastelColor = when (product.category.lowercase()) {
        "tortas" -> Color(0xFFFFF0F0)
        "pastelería" -> Color(0xFFFFF8E7)
        "panadería" -> Color(0xFFE9FFF3)
        else -> Color(0xFFFFFFFF)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = pastelColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
    ) {
        Box {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            // Badge de cantidad arriba a la derecha
            if (quantity > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color(0xFFE53935), shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("x$quantity", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(Modifier.padding(12.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text("${product.price} CLP", style = MaterialTheme.typography.titleMedium, color = Color(0xFF8D3A2E))
        }
    }
}
