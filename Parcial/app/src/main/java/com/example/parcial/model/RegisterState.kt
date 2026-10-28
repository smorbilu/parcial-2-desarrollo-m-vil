package com.example.parcial.model

import androidx.annotation.StringRes

data class RegisterState(
    val usuario: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val cargando: Boolean = false,
    val registroExito: Boolean = false,
    @StringRes val mensajeRes: Int? = null,
    val mensajeDetalle: String = ""
)
