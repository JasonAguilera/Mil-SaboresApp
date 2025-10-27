package com.example.milsaboresapp.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val DS_NAME = "user_prefs"

// DataStore instancia por-Contexto (extension property)
private val Context.dataStore by preferencesDataStore(name = DS_NAME)

class UserDataStore(private val context: Context) {

    // Claves
    private object Keys {
        val NAME = stringPreferencesKey("user_name")
        val EMAIL = stringPreferencesKey("user_email")
        val PASSWORD = stringPreferencesKey("user_password")
    }

    // Lecturas reactivas (Flow) — devuelvo String? para que puedas distinguir "vacío"
    val userName: Flow<String?> = context.dataStore.data.map { it[Keys.NAME] }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[Keys.EMAIL] }
    val userPassword: Flow<String?> = context.dataStore.data.map { it[Keys.PASSWORD] }

    // Estado de sesión como Flow<Boolean>
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        val email = prefs[Keys.EMAIL]
        val pass = prefs[Keys.PASSWORD]
        !email.isNullOrBlank() && !pass.isNullOrBlank()
    }

    // Guardar / actualizar usuario completo
    suspend fun saveUser(name: String, email: String, password: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NAME] = name
            prefs[Keys.EMAIL] = email
            prefs[Keys.PASSWORD] = password
        }
    }

    // Actualizar solo nombre (por si luego quieres editar perfil)
    suspend fun updateName(name: String) {
        context.dataStore.edit { it[Keys.NAME] = name }
    }

    // Cerrar sesión / limpiar
    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    // Helpers "one-shot" (lectura inmediata, no Flow)
    suspend fun getNameOnce(): String = userName.first().orEmpty()
    suspend fun getEmailOnce(): String = userEmail.first().orEmpty()
    suspend fun getPasswordOnce(): String = userPassword.first().orEmpty()
    suspend fun isLoggedInOnce(): Boolean = isLoggedIn.first()
}
