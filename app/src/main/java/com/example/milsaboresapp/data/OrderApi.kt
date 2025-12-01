package com.example.milsaboresapp.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// ===== DTOs que vienen del backend =====

data class CartItemRemote(
    val itemId: Long,
    val productId: Long,
    val name: String,
    val quantity: Int,
    val unitPrice: Int,
    val lineTotal: Int
)

data class CartRemote(
    val id: Long,
    val items: List<CartItemRemote>,
    val total: Int
)

// ✅ IMPORTANTE: este request SOLO lleva productId y quantity
data class AddItemRequest(
    val productId: Long,
    val quantity: Int
)

// Para /api/orders/checkout
data class CheckoutRequestRemote(
    val cartId: Long,
    val email: String,
    val fullName: String,
    val phone: String
)

data class OrderItemRemote(
    val productName: String,
    val quantity: Int,
    val unitPrice: Long,
    val lineTotal: Long
)

data class OrderRemote(
    val id: Long,
    val email: String,
    val fullName: String,
    val phone: String,
    val status: String,
    val subtotal: Int,
    val shippingCost: Int,
    val discountTotal: Int,
    val total: Int,
    val items: List<OrderItemRemote>
)

// ===== Retrofit APIs =====

interface CartApi {

    // Crea un carrito vacío
    @POST("/api/cart")
    suspend fun createCart(): CartRemote

    // Obtener carrito por id (opcional)
    @GET("/api/cart/{id}")
    suspend fun getCart(@Path("id") id: Long): CartRemote

    // ✅ Agregar ítem: fíjate en la URL
    // POST /api/cart/{id}/items
    @POST("/api/cart/{id}/items")
    suspend fun addItem(
        @Path("id") cartId: Long,
        @Body req: AddItemRequest
    ): CartRemote
}

interface OrdersApi {

    // Hace el checkout a partir del cartId
    @POST("/api/orders/checkout")
    suspend fun checkout(@Body req: CheckoutRequestRemote): OrderRemote
}
