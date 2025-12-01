package com.example.milsaboresapp.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// ====== MODELOS QUE COINCIDEN CON TU BACK ======

// Lo que tu back espera en /api/auth/login
data class LoginRequest(
    val email: String,
    val password: String
)

// Lo que tu back devuelve en /api/auth/login
// record AuthResponse(String token)
data class AuthResponse(
    val token: String
)

// Lo que tu back espera en /api/auth/register
// record RegisterRequest(String email, String password, String fullName)
data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String
)

// Lo que devuelve /api/auth/register  (MeDto)
data class MeDto(
    val id: Long,
    val email: String,
    val role: String
)

// Lo que devuelve /api/users/me  (ProfileDto)
data class ProfileDtoRemote(
    val id: Long,
    val email: String,
    val fullName: String,
    val role: String
)

// ====== MODELOS QUE USA TU UI (SE MANTIENEN IGUAL) ======

data class LoginResponse(
    val token: String,
    val fullName: String,
    val email: String,
    val role: String
)

data class RegisterResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val role: String
)

// ====== INTERFAZ RETROFIT ======

interface AuthApi {

    // POST /api/auth/login  → AuthResponse(token)
    @POST("/api/auth/login")
    suspend fun login(@Body req: LoginRequest): AuthResponse

    // POST /api/auth/register  → MeDto(id, email, role)
    @POST("/api/auth/register")
    suspend fun register(@Body req: RegisterRequest): MeDto

    // GET /api/users/me → ProfileDto(id, email, fullName, role)
    @GET("/api/users/me")
    suspend fun me(
        @Header("Authorization") authHeader: String
    ): ProfileDtoRemote
}
