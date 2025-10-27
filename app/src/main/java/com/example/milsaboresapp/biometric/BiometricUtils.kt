package com.example.milsaboresapp.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricUtils {

    fun canAuthenticate(context: Context): Boolean {
        val bm = BiometricManager.from(context)
        val result = bm.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
                    or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
        return result == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun showBiometricPrompt(
        activity: FragmentActivity,
        title: String = "Confirmar identidad",
        subtitle: String = "Usa tu huella o PIN del dispositivo",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onError(errString.toString())
            }
            override fun onAuthenticationFailed() {
                onError("Autenticación fallida")
            }
        }

        val prompt = BiometricPrompt(activity, executor, callback)

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            // Permite PIN/Patrón/Password del dispositivo como fallback
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
                        or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        prompt.authenticate(info)
    }
}
