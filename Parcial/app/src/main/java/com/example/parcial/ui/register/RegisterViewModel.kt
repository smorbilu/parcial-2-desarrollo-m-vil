package com.example.parcial.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial.R
import com.example.parcial.data.AuthRepository
import com.example.parcial.data.DatosInvalidosException
import com.example.parcial.data.SessionManager
import com.example.parcial.data.UsuarioYaExisteException
import com.example.parcial.model.RegisterState
import com.example.parcial.ui.components.mensajeRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    fun onUsuarioChange(nuevoUsuario: String) {
        _state.value = _state.value.copy(usuario = nuevoUsuario)
    }

    fun onPasswordChange(nuevoPassword: String) {
        _state.value = _state.value.copy(password = nuevoPassword)
    }

    fun onConfirmarPasswordChange(nuevoPassword: String) {
        _state.value = _state.value.copy(confirmarPassword = nuevoPassword)
    }

    fun onRegisterClic() {
        val datos = _state.value

        if (datos.cargando) return

        if (datos.usuario.isBlank() || datos.password.isEmpty()) {
            _state.value = datos.copy(mensajeRes = R.string.register_error_empty, mensajeDetalle = "")
            return
        }

        if (datos.password != datos.confirmarPassword) {
            _state.value = datos.copy(mensajeRes = R.string.register_error_password_mismatch, mensajeDetalle = "")
            return
        }

        _state.value = datos.copy(cargando = true, mensajeRes = null)

        viewModelScope.launch {
            repository.registrar(datos.usuario.trim(), datos.password)
                .onSuccess { cuerpo ->
                    SessionManager.iniciar(cuerpo.token, cuerpo.user.usuario)
                    _state.value = _state.value.copy(cargando = false, registroExito = true)
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        cargando = false,
                        registroExito = false,
                        mensajeRes = when (error) {
                            is UsuarioYaExisteException -> R.string.register_error_user_exists
                            is DatosInvalidosException -> R.string.register_error_invalid_data
                            else -> error.mensajeRes()
                        },
                        mensajeDetalle = error.message.orEmpty()
                    )
                }
        }
    }
}
