package com.example.parcial.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.parcial.model.response.UbicacionDto
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

private val CENTRO_INICIAL = LatLng(14.634262, -90.507758)
private const val ZOOM_INICIAL = 6f
private const val ZOOM_UNA_UBICACION = 15f
private const val MARGEN_BORDES_PX = 140

@Composable
fun MapaUbicaciones(
    ubicaciones: List<UbicacionDto>,
    modifier: Modifier = Modifier,
    onMarcadorClic: (UbicacionDto) -> Unit = {}
) {
    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(CENTRO_INICIAL, ZOOM_INICIAL)
    }
    var mapaListo by remember { mutableStateOf(false) }

    LaunchedEffect(mapaListo, ubicaciones) {
        if (!mapaListo || ubicaciones.isEmpty()) return@LaunchedEffect

        val posiciones = ubicaciones.map { LatLng(it.latitud, it.longitud) }.distinct()

        if (posiciones.size == 1) {
            cameraState.animate(
                CameraUpdateFactory.newLatLngZoom(posiciones.first(), ZOOM_UNA_UBICACION)
            )
        } else {
            val limites = LatLngBounds.builder().apply {
                posiciones.forEach { include(it) }
            }.build()
            cameraState.animate(CameraUpdateFactory.newLatLngBounds(limites, MARGEN_BORDES_PX))
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraState,
        onMapLoaded = { mapaListo = true }
    ) {
        ubicaciones.forEach { ubicacion ->
            key(ubicacion.id) {
                Marker(
                    state = rememberMarkerState(
                        key = ubicacion.id.toString(),
                        position = LatLng(ubicacion.latitud, ubicacion.longitud)
                    ),
                    onClick = {
                        onMarcadorClic(ubicacion)
                        true
                    }
                )
            }
        }
    }
}
