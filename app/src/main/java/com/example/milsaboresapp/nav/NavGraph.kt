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
    const val QR2FA = "qr2fa"           // 👈 nueva ruta para 2FA por QR
}

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val cartVM: CartViewModel = viewModel()

    NavHost(navController = nav, startDestination = Routes.HOME) {

        // HOME
        composable(Routes.HOME) {
            HomeScreen(
                onOpenLogin = { nav.navigate(Routes.LOGIN) },
                onOpenProducts = { nav.navigate(Routes.PRODUCTS) },
                onOpenContacto = { nav.navigate(Routes.CONTACTO) },
                onOpenNosotros = { nav.navigate(Routes.NOSOTROS) },
                onOpenCart = { nav.navigate(Routes.CART) },
                cartCount = cartVM.items.size,
                onOpenProductDetail = { id -> nav.navigate("${Routes.DETAIL}/$id") }
            )
        }

        // LISTA DE PRODUCTOS
        composable(Routes.PRODUCTS) {
            ProductsScreen(
                onBack = { nav.popBackStack() },
                onOpenCart = { nav.navigate(Routes.CART) },
                cartCount = cartVM.items.size,
                cartVM = cartVM,
                onProductClick = { product ->
                    nav.navigate("${Routes.DETAIL}/${product.id}")
                }
            )
        }

        // DETALLE DE PRODUCTO
        composable(
            route = "${Routes.DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id")
            val product = FakeRepository.getAll().firstOrNull { it.id == id }

            if (product != null) {
                ProductDetailScreen(
                    productId = product.id,
                    onAddToCart = { qty -> cartVM.add(product, qty) },
                    onBack = { nav.popBackStack() },
                    onGoProducts = { nav.popBackStack(Routes.PRODUCTS, inclusive = false) }
                )
            } else {
                nav.popBackStack()
            }
        }

        // LOGIN
        composable(Routes.LOGIN) {
            LoginScreen(
                onBack = { nav.popBackStack() },
                onGoRegister = { nav.navigate(Routes.REGISTER) },
                onLoginOk = {
                    // Cierra login y vuelve a Home
                    nav.popBackStack()
                    nav.navigate(Routes.HOME)
                },
                onGoQR = { nav.navigate(Routes.QR2FA) }   // 👈 abre el scanner QR
            )
        }

        // REGISTRO
        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { nav.popBackStack() },
                onRegistered = { nav.popBackStack() }
            )
        }

        // NOSOTROS
        composable(Routes.NOSOTROS) { NosotrosScreen(onBack = { nav.popBackStack() }) }

        // CONTACTO
        composable(Routes.CONTACTO) { ContactoScreen(onBack = { nav.popBackStack() }) }

        // CARRITO
        composable(Routes.CART) {
            CartScreen(cartVM = cartVM, onBack = { nav.popBackStack() })
        }

        // **2FA por QR**
        composable(Routes.QR2FA) {
            QRScannerScreen(
                onBack = { nav.popBackStack() },
                onCodeRead = { code ->
                    // TODO: valida el código como prefieras.
                    // Simulamos OK: volvemos al HOME
                    nav.popBackStack()
                    nav.navigate(Routes.HOME)
                }
            )
        }
    }
}
