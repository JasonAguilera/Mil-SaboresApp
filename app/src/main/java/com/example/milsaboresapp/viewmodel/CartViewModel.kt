package com.example.milsaboresapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.milsaboresapp.domain.model.Product

data class CartLine(val product: Product, val qty: Int)

class CartViewModel : ViewModel() {

    val items = mutableStateListOf<CartLine>()

    // % de descuento aplicado por QR (0 si no hay)
    var discountPct by mutableIntStateOf(0)
        private set

    fun add(product: Product, amount: Int = 1) {
        val a = amount.coerceAtLeast(1)
        val idx = items.indexOfFirst { it.product.id == product.id }
        if (idx == -1) {
            items += CartLine(product, a)
        } else {
            val current = items[idx]
            items[idx] = current.copy(qty = current.qty + a) // ← reemplaza (dispara recomposición)
        }
    }

    fun removeOne(product: Product) {
        val idx = items.indexOfFirst { it.product.id == product.id }
        if (idx == -1) return
        val current = items[idx]
        val newQty = current.qty - 1
        if (newQty <= 0) {
            items.removeAt(idx)
        } else {
            items[idx] = current.copy(qty = newQty) // ← reemplaza (dispara recomposición)
        }
    }

    fun removeAll(product: Product) {
        val idx = items.indexOfFirst { it.product.id == product.id }
        if (idx != -1) items.removeAt(idx)
    }

    fun clear() {
        items.clear()
        discountPct = 0
    }

    fun totalBeforeDiscount(): Int = items.sumOf { it.product.price * it.qty }

    fun totalAfterDiscount(): Int {
        val base = totalBeforeDiscount()
        return if (discountPct > 0) base - (base * discountPct / 100) else base
    }

    fun qtyOf(productId: String): Int =
        items.firstOrNull { it.product.id == productId }?.qty ?: 0

    /** Aplica descuento leyendo un código QR. Ajusta las reglas como quieras. */
    fun applyDiscountFromQr(code: String) {
        // Reglas demo:
        // MS-15  -> 15%  |  SABOR10 -> 10%
        // Cualquier otro que empiece por "MS-" => 5%
        discountPct = when {
            code.equals("MS-15", ignoreCase = true) -> 15
            code.equals("SABOR10", ignoreCase = true) -> 10
            code.startsWith("MS-", ignoreCase = true) -> 5
            else -> 0
        }
    }
}
