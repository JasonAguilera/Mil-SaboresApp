package com.example.milsaboresapp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity   // 👈 importante para BiometricPrompt
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.milsaboresapp.nav.AppNav

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Si tienes tu propio theme de Compose, puedes envolver aquí
            // MilSaboresTheme { ... }
            Surface(color = MaterialTheme.colorScheme.background) {
                AppNav()
            }
        }
    }
}
