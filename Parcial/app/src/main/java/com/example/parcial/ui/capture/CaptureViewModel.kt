package com.example.parcial.ui.capture

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial.R
import com.example.parcial.data.UbicacionProvider
import com.example.parcial.data.UbicacionRepository
import com.example.parcial.model.CaptureState
import com.example.parcial.ui.components.mensajeRes
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptureViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UbicacionRepository()
    private val ubicacionProvider = UbicacionProvider(application)

    private val _state = MutableStateFlow(CaptureState())
    val state: StateFlow<CaptureState> = _state

    fun onNombreChange(nuevoNombre: String) {
        _state.value = _state.value.copy(nombre = nuevoNombre)
    }

    fun onDescripcionChange(nuevaDescripcion: String) {
        _state.value = _state.value.copy(descripcion = nuevaDescripcion)
    }

    fun onUbicacionMarcada(punto: LatLng) {
        _state.value = _state.value.copy(
            ubicacionMarcada = punto,
            mensajeRes = null,
            guardadoExito = false
        )
    }

    fun onQuitarUbicacion() {
        _state.value = _state.value.copy(
            ubicacionMarcada = null,
            mensajeRes = null,
            guardadoExito = false
        )
    }

    fun formularioValido(): Boolean {
        val valido = _state.value.nombre.isNotBlank() && _state.value.descripcion.isNotBlank()

        if (!valido) {
            _state.value = _state.value.copy(
                guardadoExito = false,
                mensajeRes = R.string.capture_error_empty,
                mensajeDetalle = ""
            )
        }
        return valido
    }

    fun onPermisoDenegado() {
        _state.value = _state.value.copy(
            guardadoExito = false,
            mensajeRes = R.string.capture_error_permission,
            mensajeDetalle = ""
        )
    }

    fun onCapturarClic() {
        if (_state.value.cargando || !formularioValido()) return

        _state.value = _state.value.copy(cargando = true, mensajeRes = null, guardadoExito = false)

        viewModelScope.launch {
            val punto = _state.value.ubicacionMarcada
                ?: ubicacionProvider.obtener()?.let { LatLng(it.latitude, it.longitude) }

            if (punto == null) {
                _state.value = _state.value.copy(
                    cargando = false,
                    mensajeRes = R.string.capture_error_gps,
                    mensajeDetalle = ""
                )
                return@launch
            }

            val nombre = _state.value.nombre.trim()

            repository.guardar(
                nombre = nombre,
                descripcion = _state.value.descripcion.trim(),
                latitud = punto.latitude,
                longitud = punto.longitude
            )
                .onSuccess {
                    _state.value = CaptureState(
                        guardadoExito = true,
                        mensajeRes = R.string.capture_success,
                        mensajeDetalle = nombre
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        cargando = false,
                        guardadoExito = false,
                        mensajeRes = error.mensajeRes(),
                        mensajeDetalle = error.message.orEmpty()
                    )
                }
        }
    }
}
