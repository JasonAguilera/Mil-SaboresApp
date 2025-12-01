package com.example.milsaboresapp.data

import com.example.milsaboresapp.domain.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(
    private val api: CatalogApi = MilsaboresApi.catalog
) {

    // Carga desde el backend. Si falla, vuelve a los datos fake.
    suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            val page = api.getProducts(page = 0, size = 50)
            page.content.map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()   // o lanza el error, pero ya no uses FakeRepository
        }
    }

}
