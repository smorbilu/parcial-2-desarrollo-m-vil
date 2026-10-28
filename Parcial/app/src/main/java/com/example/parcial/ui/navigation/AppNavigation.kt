package com.example.parcial.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.parcial.data.SessionManager
import com.example.parcial.ui.capture.CaptureScreen
import com.example.parcial.ui.components.map.MapScreen
import com.example.parcial.ui.dashboard.DashboardScreen
import com.example.parcial.ui.locations.LocationsScreen
import com.example.parcial.ui.login.LoginScreen
import com.example.parcial.ui.register.RegisterScreen

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val DASHBOARD = "dashboard"
    const val CAPTURE = "capture"
    const val LOCATIONS = "locations"
    const val MAP = "map"
}

@Composable
fun AppNavigator(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN,
        modifier = modifier
    ) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onRegistroClic = { navController.navigate(AppRoutes.REGISTER) }
            )
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onVolverLogin = { navController.popBackStack() },
                onRegistroExitoso = {
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.DASHBOARD) {
            DashboardScreen(
                nombre = SessionManager.usuario,
                onCapturarClic = { navController.navigate(AppRoutes.CAPTURE) },
                onListadoClic = { navController.navigate(AppRoutes.LOCATIONS) },
                onMapaClic = { navController.navigate(AppRoutes.MAP) },
                onCerrarSesionClic = {
                    SessionManager.cerrar()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.DASHBOARD) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.CAPTURE) {
            CaptureScreen(onVolverClic = { navController.popBackStack() })
        }

        composable(AppRoutes.LOCATIONS) {
            LocationsScreen(
                onVolverClic = { navController.popBackStack() },
                onMapaClic = { navController.navigate(AppRoutes.MAP) }
            )
        }

        composable(AppRoutes.MAP) {
            MapScreen(onVolverClic = { navController.popBackStack() })
        }
    }
}
