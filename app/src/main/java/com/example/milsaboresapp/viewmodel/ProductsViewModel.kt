package com.example.milsaboresapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsaboresapp.data.ProductRepository
import com.example.milsaboresapp.domain.model.Product
import kotlinx.coroutines.launch

data class ProductsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val products: List<Product> = emptyList()
)

class ProductsViewModel(
    private val repo: ProductRepository = ProductRepository()
) : ViewModel() {

    var uiState by mutableStateOf(ProductsUiState())
        private set

    fun loadProducts() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            val list = repo.getAllProducts()
            uiState = uiState.copy(isLoading = false, products = list)
        }
    }
}
