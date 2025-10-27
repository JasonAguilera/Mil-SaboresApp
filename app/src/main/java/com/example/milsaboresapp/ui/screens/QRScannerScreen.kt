package com.example.milsaboresapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScannerScreen(
    onBack: () -> Unit,
    onCodeRead: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    var hasCamera by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> hasCamera = granted }

    LaunchedEffect(Unit) {
        if (!hasCamera) cameraPermLauncher.launch(Manifest.permission.CAMERA)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escanear cupón QR") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (!hasCamera) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Se requiere permiso de cámara")
            }
            return@Scaffold
        }

        // Vista de la cámara
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    // Preview
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    // Analyzer (ML Kit)
                    val analyzer = ImageAnalysis.Builder()
                        .setTargetResolution(Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    val options = BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE,
                            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODE_128,
                            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODE_39
                        )
                        .build()
                    val scanner = BarcodeScanning.getClient(options)

                    analyzer.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                        processImageProxy(scanner, imageProxy) { value ->
                            // Primer código leído → devolver y cerrar
                            onCodeRead(value)
                        }
                    }

                    val selector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, selector, preview, analyzer
                        )
                    } catch (_: Exception) { }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}

private fun processImageProxy(
    scanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    imageProxy: ImageProxy,
    onFound: (String) -> Unit
) {
    val mediaImage = imageProxy.image ?: run {
        imageProxy.close(); return
    }
    val rotation = imageProxy.imageInfo.rotationDegrees
    val image = InputImage.fromMediaImage(mediaImage, rotation)

    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            val first = barcodes.firstOrNull()
            val raw = first?.rawValue
            if (!raw.isNullOrBlank()) {
                onFound(raw)
            }
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}
