package com.example.parcial.data

import com.example.parcial.data.remote.RetrofitClient
import com.example.parcial.model.request.UbicacionRequest
import com.example.parcial.model.response.UbicacionDto

class UbicacionRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun guardar(
        nombre: String,
        descripcion: String,
        latitud: Double,
        longitud: Double
    ): Result<UbicacionDto> {
        return llamarApi {
            apiService.guardarUbicacion(UbicacionRequest(nombre, descripcion, latitud, longitud))
        }.mapCatching { respuesta ->
            respuesta.body ?: throw ErrorServidorException(respuesta.standardResponse.httpCode)
        }
    }

    suspend fun obtenerTodas(): Result<List<UbicacionDto>> {
        return llamarApi { apiService.obtenerUbicaciones() }
            .map { respuesta -> respuesta.body.orEmpty() }
    }
}
