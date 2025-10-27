package com.example.milsaboresapp.ui.screens

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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.R
import com.example.milsaboresapp.data.UserDataStore
import kotlinx.coroutines.launch

private val BrownRed = Color(0xFF8D3A2E)
private val AppBarWarm = Color(0xFFFFB88A)
private val AppBarWarmText = Color(0xFF5A2E1B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val userDS = remember { UserDataStore(ctx) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var showPass2 by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    fun isValidEmail(text: String): Boolean {
        // validación simple
        return android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear cuenta", color = AppBarWarmText) },
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
            // Fondo
            Image(
                painter = painterResource(R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            // Form Card
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
                        "Registro",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF222222),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Tu nombre") }
                    )
                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("tu@correo.com") }
                    )
                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = pass,
                        onValueChange = { pass = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            TextButton(onClick = { showPass = !showPass }) {
                                Text(if (showPass) "Ocultar" else "Mostrar")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("••••••••") }
                    )
                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = pass2,
                        onValueChange = { pass2 = it },
                        label = { Text("Repetir contraseña") },
                        singleLine = true,
                        visualTransformation = if (showPass2) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            TextButton(onClick = { showPass2 = !showPass2 }) {
                                Text(if (showPass2) "Ocultar" else "Mostrar")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("••••••••") }
                    )

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                when {
                                    name.isBlank() || email.isBlank() || pass.isBlank() || pass2.isBlank() ->
                                        snackbarHostState.showSnackbar("Completa todos los campos")
                                    !isValidEmail(email) ->
                                        snackbarHostState.showSnackbar("Correo inválido")
                                    pass.length < 6 ->
                                        snackbarHostState.showSnackbar("La contraseña debe tener al menos 6 caracteres")
                                    pass != pass2 ->
                                        snackbarHostState.showSnackbar("Las contraseñas no coinciden")
                                    else -> {
                                        userDS.saveUser(name.trim(), email.trim(), pass)
                                        snackbarHostState.showSnackbar("¡Cuenta creada! Ya puedes iniciar sesión.")
                                        onRegistered()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BrownRed)
                    ) {
                        Text("Registrar", color = Color.White)
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Tus datos se guardan localmente (DataStore). Puedes activar biometría/QR desde el login.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
}
