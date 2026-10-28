package com.example.parcial.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial.R
import com.example.parcial.data.AuthRepository
import com.example.parcial.data.CredencialesIncorrectasException
import com.example.parcial.data.DatosInvalidosException
import com.example.parcial.data.SessionManager
import com.example.parcial.model.LoginState
import com.example.parcial.ui.components.mensajeRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onUsuarioChange(nuevoUsuario: String) {
        _state.value = _state.value.copy(usuario = nuevoUsuario)
    }

    fun onPasswordChange(nuevoPassword: String) {
        _state.value = _state.value.copy(password = nuevoPassword)
    }

    fun onLoginClic() {
        if (_state.value.cargando) return

        val usuario = _state.value.usuario.trim()
        val password = _state.value.password

        if (usuario.isEmpty() || password.isEmpty()) {
            _state.value = _state.value.copy(
                mensajeRes = R.string.login_error_empty,
                mensajeDetalle = ""
            )
            return
        }

        _state.value = _state.value.copy(cargando = true, mensajeRes = null)

        viewModelScope.launch {
            repository.login(usuario, password)
                .onSuccess { cuerpo ->
                    SessionManager.iniciar(cuerpo.token, cuerpo.user.usuario)
                    _state.value = _state.value.copy(cargando = false, loginExito = true)
                }
                .onFailure { error ->
                    val credenciales = error is CredencialesIncorrectasException ||
                        error is DatosInvalidosException

                    _state.value = _state.value.copy(
                        cargando = false,
                        loginExito = false,
                        mensajeRes = if (credenciales) {
                            R.string.login_error_credentials
                        } else {
                            error.mensajeRes()
                        },
                        mensajeDetalle = error.message.orEmpty()
                    )
                }
        }
    }
}
