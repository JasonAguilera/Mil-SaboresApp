package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.R

private val AppBarWarm = Color(0xFFFFB88A)
private val AppBarWarmText = Color(0xFF5A2E1B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NosotrosScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Conócenos", color = AppBarWarmText) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = AppBarWarmText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBarWarm.copy(alpha = 0.9f))
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {

            Image(
                painter = painterResource(R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(18.dp))

                Text(
                    "Conócenos",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "En Mil Sabores llevamos la pasión por la repostería al siguiente nivel. Desde nuestros inicios, creamos experiencias dulces que encantan a cada cliente.",
                    color = Color(0xFFEAEAEA),
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(Modifier.height(18.dp))

                // Tarjetas Historia - Misión - Visión
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoCard(title = "Nuestra Historia", text =
                        "Pastelería Mil Sabores celebra su experiencia artesanal, renovando nuestro sistema de ventas para entregar una experiencia moderna, cercana y accesible."
                    )
                    InfoCard(title = "Misión", text =
                        "Ofrecer una experiencia dulce y memorable a nuestros clientes con tortas y productos de repostería de alta calidad, celebrando nuestras raíces."
                    )
                    InfoCard(title = "Visión", text =
                        "Ser líderes online en repostería en Chile, destacando por innovación, calidad e impacto positivo en la comunidad."
                    )
                }

                Spacer(Modifier.height(22.dp))

                // Equipo
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xF2FFFFFF)),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Conoce a nuestro equipo",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TeamItem(
                                image = R.drawable.maria, // cámbialos por tus fotos reales si quieres
                                nombre = "María González",
                                rol = "Fundadora & Chef"
                            )
                            TeamItem(
                                image = R.drawable.carlos,
                                nombre = "Carlos Pérez",
                                rol = "Maestro Pastelero"
                            )
                            TeamItem(
                                image = R.drawable.lucia,
                                nombre = "Lucía Martínez",
                                rol = "Decoradora Creativa"
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun InfoCard(title: String, text: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, color = Color(0xFF222222))
            Spacer(Modifier.height(6.dp))
            Text(text, color = Color(0xFF444444), lineHeight = 20.sp)
        }
    }
}

@Composable
private fun TeamItem(image: Int, nombre: String, rol: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(image),
            contentDescription = nombre,
            modifier = Modifier
                .size(78.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(6.dp))
        Text(nombre, fontWeight = FontWeight.Medium)
        Text(rol, color = Color(0xFF666666), fontSize = 12.sp)
    }
}
