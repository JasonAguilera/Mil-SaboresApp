package com.example.milsaboresapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsaboresapp.data.ProductRepository
import com.example.milsaboresapp.domain.model.Product
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "Todos"
)

class HomeViewModel(
    private val repo: ProductRepository = ProductRepository()
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            try {
                val list = repo.getAllProducts()

                // Armamos las categorías dinámicamente a partir de los productos
                val cats = buildList {
                    add("Todos")
                    addAll(
                        list.map { it.category }
                            .filter { it.isNotBlank() }
                            .distinct()
                    )
                }

                uiState = uiState.copy(
                    isLoading = false,
                    products = list,
                    categories = cats
                )
            } catch (e: Exception) {
                e.printStackTrace()
                uiState = uiState.copy(
                    isLoading = false,
                    error = "No se pudieron cargar los productos"
                )
            }
        }
    }

    fun selectCategory(category: String) {
        uiState = uiState.copy(selectedCategory = category)
    }
}
