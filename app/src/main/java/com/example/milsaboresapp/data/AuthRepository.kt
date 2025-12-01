package com.example.milsaboresapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Usa el mismo host/puerto del auth-service (el que usas en Postman)
private const val AUTH_BASE_URL = "http://54.147.201.123:8081/"  // IMPORTANTE: terminar en "/"

private val authRetrofit: Retrofit = Retrofit.Builder()
    .baseUrl(AUTH_BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

class AuthRepository(
    private val api: AuthApi = authRetrofit.create(AuthApi::class.java)
) {

    /**
     * LOGIN:
     * 1) POST /api/auth/login → AuthResponse(token)
     * 2) GET /api/users/me con Authorization: Bearer token → ProfileDtoRemote
     * 3) Devolvemos LoginResponse con todo listo para la UI
     */
    suspend fun login(email: String, password: String): LoginResponse {
        // Paso 1: obtener token
        val auth = api.login(LoginRequest(email = email, password = password))

        // Paso 2: obtener perfil usando ese token
        val profile = api.me("Bearer ${auth.token}")

        // Paso 3: devolver modelo que tu LoginScreen ya usa
        return LoginResponse(
            token = auth.token,
            fullName = profile.fullName,
            email = profile.email,
            role = profile.role
        )
    }

    /**
     * REGISTER:
     * 1) POST /api/auth/register → MeDto(id, email, role)
     * 2) Construimos RegisterResponse usando el fullName que enviamos
     */
    suspend fun register(fullName: String, email: String, password: String): RegisterResponse {
        val me = api.register(
            RegisterRequest(
                email = email,
                password = password,
                fullName = fullName
            )
        )

        return RegisterResponse(
            id = me.id,
            fullName = fullName,   // el back no lo devuelve, así que usamos el que enviamos
            email = me.email,
            role = me.role
        )
    }
}
