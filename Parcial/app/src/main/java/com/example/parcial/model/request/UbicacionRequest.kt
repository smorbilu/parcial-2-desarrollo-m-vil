package com.example.parcial.model.request

data class UbicacionRequest(
    val nombre: String,
    val descripcion: String,
    val latitud: Double,
    val longitud: Double
)
