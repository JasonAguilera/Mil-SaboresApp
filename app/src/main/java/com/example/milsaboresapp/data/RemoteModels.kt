package com.example.milsaboresapp.data

import com.example.milsaboresapp.domain.model.Product
import java.math.BigDecimal

// MISMO dominio que estás usando en la web
private const val IMAGE_BASE_URL =
    "http://milsabores-web.s3-website-us-east-1.amazonaws.com"

// Coincide con PageResponse<T> del back
data class PageResponseDto<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)

// Debe calzar con tu ProductDTO del catalog-service
data class ProductDto(
    val id: Long,
    val sku: String,
    val name: String,
    val description: String?,
    val price: BigDecimal?,
    val stock: Int?,
    val imageUrl: String?,      // 👈 este campo viene del back
    val active: Boolean?,
    val categoryId: Long?,
    val categoryName: String?
)

/**
 * Arma una URL completa de imagen.
 * Casos:
 * - Si viene null o vacío → devolvemos "" (no hay imagen).
 * - Si ya viene con http → la dejamos tal cual.
 * - Si viene "/img/scon_blu.png" → pegamos BASE + path.
 * - Si viene "img/scon_blu.png" → BASE + "/" + path.
 */
private fun buildImageUrl(path: String?): String {
    if (path.isNullOrBlank()) return ""

    if (path.startsWith("http")) return path

    return if (path.startsWith("/")) {
        IMAGE_BASE_URL + path
    } else {
        "$IMAGE_BASE_URL/$path"
    }
}

// Mapeo DTO → modelo que usa tu app
fun ProductDto.toDomain(): Product = Product(
    id = id.toString(),
    name = name,
    price = (price ?: BigDecimal.ZERO).toInt(),
    imageUrl = buildImageUrl(imageUrl),
    description = description ?: "",
    category = categoryName ?: ""
)
