package com.example.parcial.data

import kotlinx.coroutines.CancellationException
import retrofit2.Response
import java.io.IOException

class CredencialesIncorrectasException : Exception()

class SesionInvalidaException : Exception()

class DatosInvalidosException : Exception()

class UsuarioYaExisteException : Exception()

class ErrorServidorException(codigo: Int) : Exception(codigo.toString())

class ErrorConexionException(detalle: String?) : Exception(detalle)

class UbicacionNoDisponibleException : Exception()

internal suspend fun <T> llamarApi(
    autenticada: Boolean = true,
    bloque: suspend () -> Response<T>
): Result<T> {
    return try {
        val response = bloque()
        val cuerpo = response.body()

        when {
            response.isSuccessful && cuerpo != null -> Result.success(cuerpo)
            response.code() == 401 -> Result.failure(
                if (autenticada) SesionInvalidaException() else CredencialesIncorrectasException()
            )
            response.code() == 400 -> Result.failure(DatosInvalidosException())
            response.code() == 409 -> Result.failure(UsuarioYaExisteException())
            else -> Result.failure(ErrorServidorException(response.code()))
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        Result.failure(ErrorConexionException(e.message))
    } catch (e: Exception) {
        Result.failure(ErrorServidorException(0))
    }
}
