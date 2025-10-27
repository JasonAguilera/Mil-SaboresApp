package com.example.milsaboresapp.data

import com.example.milsaboresapp.domain.model.Product

object FakeRepository {

    private const val PKG = "com.example.milsaboresapp"

    // 🔹 Todos los productos disponibles
    private val products = listOf(
        Product(
            id = "p_cheesecake",
            name = "Cheesecake",
            price = 15990,
            imageUrl = "android.resource://$PKG/drawable/cheese",
            description = "Tarta de queso clásica con base de galleta y coulis de frutos rojos.",
            category = "pastelería"
        ),
        Product(
            id = "t_selva",
            name = "Selva Negra",
            price = 17990,
            imageUrl = "android.resource://$PKG/drawable/torta",
            description = "Bizcocho de chocolate, crema chantilly y guindas.",
            category = "tortas"
        ),
        Product(
            id = "p_browni",
            name = "Brownie de Chocolate",
            price = 2490,
            imageUrl = "android.resource://$PKG/drawable/browni",
            description = "Brownie húmedo con cacao intenso y trozos de nuez.",
            category = "pastelería"
        ),
        Product(
            id = "p_carrot",
            name = "Carrot Cake",
            price = 16990,
            imageUrl = "android.resource://$PKG/drawable/carrot",
            description = "Bizcocho de zanahoria con frosting de queso crema y nueces.",
            category = "tortas"
        ),
        Product(
            id = "p_blondi",
            name = "Blondie",
            price = 2490,
            imageUrl = "android.resource://$PKG/drawable/blondi",
            description = "Brownie rubio con chips de chocolate blanco y vainilla.",
            category = "pastelería"
        ),
        Product(
            id = "p_redvelvet",
            name = "Red Velvet",
            price = 17990,
            imageUrl = "android.resource://$PKG/drawable/redvelvet",
            description = "Bizcocho rojo aterciopelado con frosting de crema de queso.",
            category = "tortas"
        ),
        Product(
            id = "p_pavlova",
            name = "Pavlova",
            price = 15990,
            imageUrl = "android.resource://$PKG/drawable/pavlova",
            description = "Base de merengue crujiente con crema batida y frutas frescas.",
            category = "pastelería"
        ),
        Product(
            id = "p_cinnamon",
            name = "Cinnamon Roll",
            price = 1990,
            imageUrl = "android.resource://$PKG/drawable/cinnamon",
            description = "Roll de canela con glaseado de vainilla.",
            category = "panadería"
        ),
        Product(
            id = "p_croasan",
            name = "Croissant Mantequilla",
            price = 1490,
            imageUrl = "android.resource://$PKG/drawable/croasan",
            description = "Clásico croissant francés, hojaldrado y dorado.",
            category = "panadería"
        ),
        Product(
            id = "p_bluebagel",
            name = "Blueberry Bagel",
            price = 2290,
            imageUrl = "android.resource://$PKG/drawable/bluebagel",
            description = "Bagel de arándanos, perfecto para desayuno o brunch.",
            category = "panadería"
        ),
        Product(
            id = "p_galleta",
            name = "Galleta Chips",
            price = 990,
            imageUrl = "android.resource://$PKG/drawable/galleta",
            description = "Galleta casera con chips de chocolate.",
            category = "pastelería"
        ),
        Product(
            id = "p_macarons",
            name = "Macarons Franceses",
            price = 4990,
            imageUrl = "android.resource://$PKG/drawable/macarons",
            description = "Asortado de macarons con rellenos artesanales.",
            category = "pastelería"
        ),
        Product(
            id = "p_scon_blu",
            name = "Scon Blueberry",
            price = 1990,
            imageUrl = "android.resource://$PKG/drawable/scon_blu",
            description = "Scon con arándanos y ligero glaseado.",
            category = "panadería"
        ),
        Product(
            id = "p_blog",
            name = "Pastel de la Casa",
            price = 15990,
            imageUrl = "android.resource://$PKG/drawable/blog",
            description = "Torta especial de la casa con diseño moderno y sabor vainilla.",
            category = "tortas"
        )
    )

    // 🔸 Todos los productos
    fun getAll(): List<Product> = products

    // 🔸 Productos destacados para el carrusel
    fun getRecommended(): List<Product> = products.filter {
        it.id in listOf("p_cheesecake", "t_selva", "p_redvelvet", "p_blog")
    }

    // 🔸 Productos por categoría (útil para filtros)
    fun getByCategory(category: String): List<Product> =
        products.filter { it.category.equals(category, ignoreCase = true) }
}
