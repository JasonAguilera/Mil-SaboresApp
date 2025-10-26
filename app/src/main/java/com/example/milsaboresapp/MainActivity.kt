package com.example.milsaboresapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.milsaboresapp.nav.AppNav

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Sin tema personalizado, usamos los valores por defecto de Material3
            Surface(color = MaterialTheme.colorScheme.background) {
                AppNav()
            }
        }
    }
}