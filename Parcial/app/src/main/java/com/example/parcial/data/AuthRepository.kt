package com.example.parcial.data

import com.example.parcial.data.remote.RetrofitClient
import com.example.parcial.model.request.LoginRequest
import com.example.parcial.model.request.RegisterRequest
import com.example.parcial.model.response.LoginBody

class AuthRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun login(usuario: String, password: String): Result<LoginBody> {
        return llamarApi(autenticada = false) {
            apiService.login(LoginRequest(usuario, password))
        }.mapCatching { respuesta ->
            respuesta.body ?: throw ErrorServidorException(respuesta.standardResponse.httpCode)
        }
    }

    suspend fun registrar(usuario: String, password: String): Result<LoginBody> {
        return llamarApi(autenticada = false) {
            apiService.register(RegisterRequest(usuario, password))
        }.mapCatching { respuesta ->
            respuesta.body ?: throw ErrorServidorException(respuesta.standardResponse.httpCode)
        }
    }
}
