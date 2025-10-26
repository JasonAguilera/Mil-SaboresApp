package com.example.milsaboresapp.ui.screens

import androidx.fragment.app.FragmentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.R
import com.example.milsaboresapp.biometric.BiometricUtils

private val BrownRed = Color(0xFF8D3A2E)
private val AppBarWarm = Color(0xFFFFB88A)
private val AppBarWarmText = Color(0xFF5A2E1B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onGoRegister: () -> Unit,
    onLoginOk: () -> Unit,
    onGoQR: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val ctx = LocalContext.current
    var bioMsg by remember { mutableStateOf<String?>(null) }

    // si NO es FragmentActivity, ocultamos el botón biométrico
    val fragmentActivity = ctx as? FragmentActivity
    val canUseBiometrics = fragmentActivity != null && BiometricUtils.canAuthenticate(ctx)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Iniciar sesión", color = AppBarWarmText) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = AppBarWarmText
                        )
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
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            // Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                colors = CardDefaults.cardColors(containerColor = Color(0xF2FFFFFF)),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "Bienvenido",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF222222),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(12.dp))

                    Text("Correo", fontWeight = FontWeight.SemiBold)
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        placeholder = { Text("tu@correo.com") }
                    )
                    Spacer(Modifier.height(10.dp))

                    Text("Contraseña", fontWeight = FontWeight.SemiBold)
                    TextField(
                        value = pass,
                        onValueChange = { pass = it },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        placeholder = { Text("••••••••") }
                    )

                    error?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, color = Color.Red)
                    }

                    Spacer(Modifier.height(16.dp))

                    // Botón login normal
                    Button(
                        onClick = {
                            if (email.isBlank() || pass.isBlank()) {
                                error = "Completa correo y contraseña"
                            } else {
                                error = null
                                onLoginOk()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BrownRed)
                    ) {
                        Text("Iniciar sesión", color = Color.White)
                    }

                    // Biometría (solo si tenemos FragmentActivity y device soporta)
                    Spacer(Modifier.height(12.dp))
                    if (canUseBiometrics) {
                        Button(
                            onClick = {
                                val act = fragmentActivity
                                if (act != null) {
                                    BiometricUtils.showBiometricPrompt(
                                        activity = act,
                                        onSuccess = {
                                            bioMsg = null
                                            onLoginOk()
                                        },
                                        onError = { msg -> bioMsg = msg }
                                    )
                                } else {
                                    bioMsg = "Esta pantalla no soporta biometría."
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = BrownRed)
                        ) {
                            Text("Iniciar sesión con biometría", color = Color.White)
                        }
                    }

                    bioMsg?.let {
                        Spacer(Modifier.height(6.dp))
                        Text(it, color = Color.Red)
                    }

                    // 2FA con cámara (QR)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onGoQR,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Verificar 2FA con cámara (QR)")
                    }

                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = onGoRegister, modifier = Modifier.align(Alignment.End)) {
                        Text("Crear cuenta")
                    }
                }
            }
        }
    }
}
