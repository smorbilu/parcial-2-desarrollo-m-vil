package com.example.parcial.ui.components.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial.data.UbicacionRepository
import com.example.parcial.model.MapState
import com.example.parcial.model.response.UbicacionDto
import com.example.parcial.ui.components.mensajeRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private val repository = UbicacionRepository()

    private val _state = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = _state

    init {
        cargarUbicaciones()
    }

    fun cargarUbicaciones() {
        if (_state.value.cargando) return

        _state.value = _state.value.copy(cargando = true, mensajeRes = null)

        viewModelScope.launch {
            repository.obtenerTodas()
                .onSuccess { ubicaciones ->
                    _state.value = _state.value.copy(
                        ubicaciones = ubicaciones,
                        cargando = false,
                        cargadas = true
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        cargando = false,
                        mensajeRes = error.mensajeRes(),
                        mensajeDetalle = error.message.orEmpty()
                    )
                }
        }
    }

    fun onMarcadorClic(ubicacion: UbicacionDto) {
        _state.value = _state.value.copy(
            ubicacionSeleccionada = ubicacion,
            dialogoVisible = true
        )
    }

    fun cerrarDialogo() {
        _state.value = _state.value.copy(
            ubicacionSeleccionada = null,
            dialogoVisible = false
        )
    }
}
