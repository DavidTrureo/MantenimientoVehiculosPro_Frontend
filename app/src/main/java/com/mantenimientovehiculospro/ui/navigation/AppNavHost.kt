package com.mantenimientovehiculospro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mantenimientovehiculospro.ui.screens.AddVehiculoScreen
import com.mantenimientovehiculospro.ui.screens.CrearMantenimientoScreen
import com.mantenimientovehiculospro.ui.screens.DetalleVehiculoScreen
import com.mantenimientovehiculospro.ui.screens.EditarMantenimientoScreen
import com.mantenimientovehiculospro.ui.screens.EditarVehiculoScreen
import com.mantenimientovehiculospro.ui.screens.QrScannerScreen
import com.mantenimientovehiculospro.ui.screens.VehiculoScreen

// Este es el controlador principal de la navegación de la app.
// Básicamente, es el que sabe qué pantalla mostrar en cada momento.
@Composable
fun AppNavHost(usuarioId: Long) {
    // El navController es el "cerebro" que se encarga de cambiar de pantalla.
    val navController = rememberNavController()

    // NavHost es el contenedor donde se van a ir mostrando las diferentes pantallas.
    NavHost(
        navController = navController,
        // Le digo cuál es la primera pantalla que se debe mostrar al entrar aquí.
        startDestination = "vehiculo_list"
    ) {
        // --- Defino cada una de las "rutas" o pantallas de mi app ---

        // Ruta para la pantalla principal, que muestra la lista de vehículos.
        composable("vehiculo_list") {
            VehiculoScreen(
                // Si le doy al botón de añadir, me lleva a la pantalla "addVehiculo".
                onAddVehiculoClick = { navController.navigate("addVehiculo") },
                // Si toco un vehículo de la lista, me lleva a su detalle, pasándole el ID.
                onVehiculoClick = { id -> navController.navigate("detalleVehiculo/$id") },
                // TODO: Si cierro sesión, debería llevarme a la pantalla de login.
                onLogout = { /* navController.navigate("login") */ },
                navController = navController
            )
        }

        // Ruta para la pantalla de añadir un nuevo vehículo.
        composable("addVehiculo") {
            AddVehiculoScreen(
                // Cuando guardo el vehículo, vuelvo a la pantalla anterior (la lista).
                onVehiculoGuardado = { navController.popBackStack() },
                // Si presiono la flecha de "atrás", también vuelvo.
                onBack = { navController.popBackStack() }
            )
        }

        // Ruta para la pantalla que escanea códigos QR.
        composable("scanner") {
            QrScannerScreen(
                // Cuando el escáner lee un QR...
                onQrScanned = { qrValue ->
                    // Intento sacar el ID del vehículo del texto del QR.
                    val id = parseVehiculoId(qrValue)
                    // Si lo consigo, navego a la pantalla de detalle de ese vehículo.
                    if (id != null) {
                        navController.navigate("detalleVehiculo/$id")
                    }
                },
                // Si cierro el escáner, vuelvo a la pantalla anterior.
                onClose = { navController.popBackStack() }
            )
        }

        // Ruta para la pantalla de detalle de un vehículo.
        composable(
            route = "detalleVehiculo/{vehiculoId}",
            arguments = listOf(navArgument("vehiculoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val vehiculoId = backStackEntry.arguments!!.getLong("vehiculoId")
            DetalleVehiculoScreen(
                vehiculoId = vehiculoId,
                onBack = { navController.popBackStack() },
                onEditar = { id -> navController.navigate("editarVehiculo/$id") },
                onAgregarMantenimiento = { id -> navController.navigate("crearMantenimiento/$id") },
                onEditarMantenimiento = { id -> navController.navigate("editarMantenimiento/$id") },
                onEliminarMantenimiento = { id -> false } // Se maneja internamente
            )
        }

        // NUEVA RUTA: Editar Vehículo
        composable(
            route = "editarVehiculo/{vehiculoId}",
            arguments = listOf(navArgument("vehiculoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val vehiculoId = backStackEntry.arguments!!.getLong("vehiculoId")
            EditarVehiculoScreen(
                vehiculoId = vehiculoId,
                navController = navController // Pasamos el controlador
            )
        }

        // NUEVA RUTA: Crear Mantenimiento
        composable(
            route = "crearMantenimiento/{vehiculoId}",
            arguments = listOf(navArgument("vehiculoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val vehiculoId = backStackEntry.arguments!!.getLong("vehiculoId")
            CrearMantenimientoScreen(
                vehiculoId = vehiculoId,
                navController = navController // Pasamos el controlador
            )
        }

        // NUEVA RUTA: Editar Mantenimiento
        composable(
            route = "editarMantenimiento/{mantenimientoId}",
            arguments = listOf(navArgument("mantenimientoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val mantenimientoId = backStackEntry.arguments!!.getLong("mantenimientoId")
            EditarMantenimientoScreen(
                mantenimientoId = mantenimientoId,
                navController = navController // Pasamos el controlador
            )
        }
    }
}

// Una función pequeña que hice para "limpiar" el texto que me da el QR.
private fun parseVehiculoId(qrValue: String): Long? {
    return if (qrValue.startsWith("VEHICULO:")) {
        qrValue.removePrefix("VEHICULO:").toLongOrNull()
    } else {
        // Intento por si es una URL, agarro lo último después del último '/'
        qrValue.split('/').lastOrNull()?.toLongOrNull()
    }
}
