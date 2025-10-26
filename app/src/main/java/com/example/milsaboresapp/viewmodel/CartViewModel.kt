package com.example.milsaboresapp.viewmodel

import androidx.compose.runtime.mutableStateListOfimport androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    private val _items = mutableStateListOf<Product>()
    val items: List<Product> get() = _items

    fun add(product: Product) {
        _items.add(product)
    }

    fun add(product: Product, qty: Int) {
        repeat(qty) { _items.add(product) }
    }

    fun remove(product: Product) {
        _items.remove(product)
    }

    fun clear() {
        _items.clear()
    }

    fun total(): Int = _items.sumOf { it.price }
}