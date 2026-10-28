package com.example.parcial.data

object SessionManager {

    @Volatile
    var token: String = ""
        private set

    @Volatile
    var usuario: String = ""
        private set

    fun iniciar(nuevoToken: String, nuevoUsuario: String) {
        token = nuevoToken
        usuario = nuevoUsuario
    }

    fun cerrar() {
        token = ""
        usuario = ""
    }
}
