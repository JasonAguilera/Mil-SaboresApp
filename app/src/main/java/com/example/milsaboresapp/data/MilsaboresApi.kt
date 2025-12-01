package com.example.milsaboresapp.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 👇 CAMBIA ESTO por tu IP/host real del catalog-service
// Ejemplo: "http://52.xx.xx.xx:8080"
private const val BASE_URL = "http://54.147.201.123:8082"

// --- Retrofit básico ---
private val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(httpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// --- API del catálogo ---
interface CatalogApi {

    @GET("/api/products")
    suspend fun getProducts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 50,
        @Query("q") q: String? = null
    ): PageResponseDto<ProductDto>
}

// Exponer una instancia lista para usar
object MilsaboresApi {
    val catalog: CatalogApi = retrofit.create(CatalogApi::class.java)
}
