package com.example.parcial.ui.components

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatearCoordenadas(latitud: Double, longitud: Double): String =
    String.format(Locale.US, "%.6f, %.6f", latitud, longitud)

fun formatearFecha(fechaIso: String?): String {
    if (fechaIso.isNullOrBlank() || fechaIso.length < 19) return fechaIso.orEmpty()

    return try {
        val entrada = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val fecha = entrada.parse(fechaIso.substring(0, 19)) ?: return fechaIso
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(fecha)
    } catch (e: Exception) {
        fechaIso
    }
}
