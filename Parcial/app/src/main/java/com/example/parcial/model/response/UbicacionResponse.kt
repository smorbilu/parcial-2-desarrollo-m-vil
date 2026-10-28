package com.example.parcial.model.response

data class UbicacionDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val creadoEn: String?
)

data class UbicacionResponse(
    val standardResponse: StandardResponse,
    val body: UbicacionDto?
)

data class UbicacionesResponse(
    val standardResponse: StandardResponse,
    val body: List<UbicacionDto>?
)
