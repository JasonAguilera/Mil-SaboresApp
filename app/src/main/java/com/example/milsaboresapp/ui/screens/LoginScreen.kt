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
import com.example.milsaboresapp.data.UserDataStore
import kotlinx.coroutines.launch

private val BrownRed = Color(0xFF8D3A2E)
private val AppBarWarm = Color(0xFFFFB88A)
private val AppBarWarmText = Color(0xFF5A2E1B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onGoRegister: () -> Unit,
    onLoginOk: () -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val userDS = remember { UserDataStore(ctx) }

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    val savedEmail by userDS.userEmail.collectAsState(initial = "")
    val savedPassword by userDS.userPassword.collectAsState(initial = "")
    val snackbarHostState = remember { SnackbarHostState() }

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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            Image(
                painter = painterResource(R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

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

                    OutlinedTextField(
                        value = email, onValueChange = { email = it },
                        label = { Text("Correo") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("tu@correo.com") }
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = pass, onValueChange = { pass = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("••••••••") }
                    )

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                val e = email.trim()
                                val p = pass
                                val se = savedEmail?.trim().orEmpty()
                                val sp = savedPassword.orEmpty()

                                when {
                                    e.isBlank() || p.isBlank() -> snackbarHostState.showSnackbar("Completa correo y contraseña")
                                    se.isBlank() || sp.isBlank() -> snackbarHostState.showSnackbar("Aún no hay usuario registrado")
                                    e.equals(se, ignoreCase = true) && p == sp -> {
                                        snackbarHostState.showSnackbar("Inicio de sesión exitoso")
                                        onLoginOk()
                                    }
                                    else -> snackbarHostState.showSnackbar("Credenciales incorrectas")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BrownRed)
                    ) {
                        Text("Iniciar sesión", color = Color.White)
                    }

                    if (canUseBiometrics) {
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = {
                                fragmentActivity?.let { act ->
                                    BiometricUtils.showBiometricPrompt(
                                        activity = act,
                                        onSuccess = {
                                            scope.launch { snackbarHostState.showSnackbar("Autenticación biométrica OK") }
                                            onLoginOk()
                                        },
                                        onError = { msg ->
                                            scope.launch { snackbarHostState.showSnackbar(msg) }
                                        }
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = BrownRed)
                        ) {
                            Text("Iniciar sesión con biometría", color = Color.White)
                        }
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