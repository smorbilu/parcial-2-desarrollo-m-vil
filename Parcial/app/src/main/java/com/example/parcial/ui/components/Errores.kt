package com.example.parcial.ui.components

import androidx.annotation.StringRes
import com.example.parcial.R
import com.example.parcial.data.DatosInvalidosException
import com.example.parcial.data.ErrorServidorException
import com.example.parcial.data.SesionInvalidaException

@StringRes
fun Throwable.mensajeRes(): Int = when (this) {
    is SesionInvalidaException -> R.string.error_session
    is DatosInvalidosException -> R.string.error_invalid
    is ErrorServidorException -> R.string.error_server
    else -> R.string.error_connection
}
