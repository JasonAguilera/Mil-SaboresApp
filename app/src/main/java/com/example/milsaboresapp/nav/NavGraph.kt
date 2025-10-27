package com.example.milsaboresapp.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.milsaboresapp.data.FakeRepository
import com.example.milsaboresapp.ui.screens.*
import com.example.milsaboresapp.viewmodel.CartViewModel

object Routes {
    const val HOME = "home"
    const val PRODUCTS = "products"
    const val DETAIL = "detail"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val NOSOTROS = "nosotros"
    const val CONTACTO = "contacto"
    const val CART = "cart"
    const val QR = "qr"
}

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val cartVM: CartViewModel = viewModel()

    NavHost(navController = nav, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onOpenLogin = { nav.navigate(Routes.LOGIN) },
                onOpenProducts = { nav.navigate(Routes.PRODUCTS) },
                onOpenContacto = { nav.navigate(Routes.CONTACTO) },
                onOpenNosotros = { nav.navigate(Routes.NOSOTROS) },
                onOpenCart = { nav.navigate(Routes.CART) },
                onLoggedOut = {
                    nav.navigate(Routes.LOGIN) { popUpTo(Routes.HOME) { inclusive = true } }
                },
                onOpenProductDetail = { id -> nav.navigate("${Routes.DETAIL}/$id") },
                cartCount = cartVM.items.sumOf { it.qty }
            )
        }

        composable(Routes.PRODUCTS) {
            ProductsScreen(
                onBack = { nav.popBackStack() },
                onOpenCart = { nav.navigate(Routes.CART) },
                cartCount = cartVM.items.sumOf { it.qty },
                onProductClick = { p -> nav.navigate("${Routes.DETAIL}/${p.id}") }
            )
        }

        composable(
            route = "${Routes.DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id")
            val product = FakeRepository.getAll().firstOrNull { it.id == id }
            if (product != null) {
                ProductDetailScreen(
                    productId = product.id,
                    onAddToCart = { qty ->
                        cartVM.add(product, qty)
                        nav.popBackStack()
                        nav.navigate(Routes.PRODUCTS)
                    },
                    onBack = { nav.popBackStack() },
                    onGoProducts = { nav.navigate(Routes.PRODUCTS) }

                )
            } else {
                nav.popBackStack()
            }
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onBack = { nav.popBackStack() },
                onGoRegister = { nav.navigate(Routes.REGISTER) },
                onLoginOk = { nav.navigate(Routes.HOME) { popUpTo(0) } },
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { nav.popBackStack() },
                onRegistered = { nav.popBackStack() }
            )
        }

        composable(Routes.NOSOTROS) { NosotrosScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.CONTACTO) { ContactoScreen(onBack = { nav.popBackStack() }) }

        // CARRITO con botón de QR integrado
        composable(Routes.CART) {
            CartScreen(
                cartVM = cartVM,
                onBack = { nav.popBackStack() },
                onScanQr = { nav.navigate(Routes.QR) }
            )
        }

        // Lector QR (placeholder actual)
        composable(Routes.QR) {
            QRScannerScreen(
                onBack = { nav.popBackStack() },
                onCodeRead = { code ->
                    cartVM.applyDiscountFromQr(code)
                    nav.popBackStack() // vuelve al carrito mostrando el descuento aplicado
                }
            )
        }
    }
}
