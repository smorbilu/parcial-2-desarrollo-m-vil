package com.example.parcial.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class UbicacionProvider(context: Context) {

    private val cliente = LocationServices.getFusedLocationProviderClient(context)

    suspend fun obtener(): Location? {
        val actual = withTimeoutOrNull(TIEMPO_ESPERA_MS) { ubicacionActual() }
        return actual ?: ultimaConocida()
    }

    @SuppressLint("MissingPermission")
    private suspend fun ubicacionActual(): Location? =
        suspendCancellableCoroutine { continuacion ->
            val cancelacion = CancellationTokenSource()
            continuacion.invokeOnCancellation { cancelacion.cancel() }

            cliente.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancelacion.token)
                .addOnSuccessListener { ubicacion ->
                    if (continuacion.isActive) continuacion.resume(ubicacion)
                }
                .addOnFailureListener {
                    if (continuacion.isActive) continuacion.resume(null)
                }
        }

    @SuppressLint("MissingPermission")
    private suspend fun ultimaConocida(): Location? =
        suspendCancellableCoroutine { continuacion ->
            cliente.lastLocation
                .addOnSuccessListener { ubicacion ->
                    if (continuacion.isActive) continuacion.resume(ubicacion)
                }
                .addOnFailureListener {
                    if (continuacion.isActive) continuacion.resume(null)
                }
        }

    private companion object {
        const val TIEMPO_ESPERA_MS = 12_000L
    }
}
