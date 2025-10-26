package com.example.milsaboresapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.R

private val BrownRed = Color(0xFF8D3A2E)
private val AppBarWarm = Color(0xFFFFB88A)
private val AppBarWarmText = Color(0xFF5A2E1B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear cuenta", color = AppBarWarmText) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = AppBarWarmText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBarWarm.copy(alpha = 0.9f))
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Registro", style = MaterialTheme.typography.headlineSmall, color = BrownRed)
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                        OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") })
                        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") })
                        OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") })
                        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                        OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation())

                        if (error != null) {
                            Spacer(Modifier.height(8.dp))
                            Text(error!!, color = MaterialTheme.colorScheme.error)
                        }

                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                when {
                                    nombre.isBlank() || apellido.isBlank() -> error = "Completa todos los campos"
                                    !email.contains("@") -> error = "Email inválido"
                                    pass.length < 6 -> error = "Contraseña muy corta"
                                    else -> { error = null; onRegistered() }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = BrownRed, contentColor = Color.White)
                        ) {
                            Text("Registrar")
                        }
                    }
                }
            }
        }
    }
}
