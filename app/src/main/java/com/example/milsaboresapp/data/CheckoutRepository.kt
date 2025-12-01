package com.example.milsaboresapp.data

import com.example.milsaboresapp.viewmodel.CartLine
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 👇 Usa la MISMA URL que ya usas para productos en ProductRepository
// Ejemplo si tu back está en EC2:
private const val BASE_URL = "http://54.147.201.123:8083"
// o la que tengas en ProductRepository

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

class CheckoutRepository(
    private val cartApi: CartApi = retrofit.create(CartApi::class.java),
    private val ordersApi: OrdersApi = retrofit.create(OrdersApi::class.java)
) {

    /**
     * Flujo:
     * 1) Crea carrito en el backend
     * 2) Agrega cada item del carrito local al carrito remoto
     * 3) Llama a /api/orders/checkout
     */
    suspend fun checkoutCart(
        cartLines: List<CartLine>,
        fullName: String,
        email: String,
        phone: String
    ): OrderRemote {

        // 1) Crear carrito vacío en el back
        val cart = cartApi.createCart()
        val cartId = cart.id

        // 2) Agregar cada item del carrito local
        for (line in cartLines) {
            val productId = line.product.id.toLongOrNull()
                ?: throw IllegalArgumentException("ID de producto inválido: ${line.product.id}")

            cartApi.addItem(
                cartId,
                AddItemRequest(
                    productId = productId,
                    quantity = line.qty
                )
            )
        }

        // 3) Hacer checkout
        val order = ordersApi.checkout(
            CheckoutRequestRemote(
                cartId = cartId,
                email = email,
                fullName = fullName,
                phone = phone
            )
        )

        return order
    }
}
