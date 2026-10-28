package com.example.parcial.model

import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng

data class CaptureState(
    val nombre: String = "",
    val descripcion: String = "",
    val ubicacionMarcada: LatLng? = null,
    val cargando: Boolean = false,
    val guardadoExito: Boolean = false,
    @StringRes val mensajeRes: Int? = null,
    val mensajeDetalle: String = ""
)
