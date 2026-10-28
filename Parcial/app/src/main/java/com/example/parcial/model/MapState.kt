package com.example.parcial.model

import androidx.annotation.StringRes
import com.example.parcial.model.response.UbicacionDto

data class MapState(
    val ubicaciones: List<UbicacionDto> = emptyList(),
    val cargando: Boolean = false,
    val cargadas: Boolean = false,
    val ubicacionSeleccionada: UbicacionDto? = null,
    val dialogoVisible: Boolean = false,
    @StringRes val mensajeRes: Int? = null,
    val mensajeDetalle: String = ""
)
