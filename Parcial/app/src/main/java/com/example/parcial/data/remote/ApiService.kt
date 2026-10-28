package com.example.parcial.data.remote

import com.example.parcial.model.request.LoginRequest
import com.example.parcial.model.request.RegisterRequest
import com.example.parcial.model.request.UbicacionRequest
import com.example.parcial.model.response.LoginResponse
import com.example.parcial.model.response.UbicacionResponse
import com.example.parcial.model.response.UbicacionesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @POST("api/ubicaciones")
    suspend fun guardarUbicacion(@Body request: UbicacionRequest): Response<UbicacionResponse>

    @GET("api/ubicaciones")
    suspend fun obtenerUbicaciones(): Response<UbicacionesResponse>
}
