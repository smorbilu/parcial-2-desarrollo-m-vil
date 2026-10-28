package com.example.parcial.model

import androidx.annotation.StringRes

data class LoginState(
    val usuario: String = "",
    val password: String = "",
    val cargando: Boolean = false,
    val loginExito: Boolean = false,
    @StringRes val mensajeRes: Int? = null,
    val mensajeDetalle: String = ""
)
