package com.example.parcial.model.response

data class UserResponse(
    val id: Int,
    val usuario: String
)

data class LoginBody(
    val token: String,
    val user: UserResponse
)

data class LoginResponse(
    val standardResponse: StandardResponse,
    val body: LoginBody?
)
