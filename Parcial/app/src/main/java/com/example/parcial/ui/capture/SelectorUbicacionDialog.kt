package com.example.parcial.ui.capture

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.parcial.R
import com.example.parcial.data.UbicacionProvider
import com.example.parcial.ui.components.formatearCoordenadas
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private val CIUDAD_DE_GUATEMALA = LatLng(14.634262, -90.507758)

@Composable
fun SelectorUbicacionDialog(
    ubicacionInicial: LatLng?,
    ubicacionProvider: UbicacionProvider,
    permisoUbicacion: Boolean,
    onConfirmar: (LatLng) -> Unit,
    onCancelar: () -> Unit
) {
    var marcada by remember { mutableStateOf(ubicacionInicial) }

    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            ubicacionInicial ?: CIUDAD_DE_GUATEMALA,
            if (ubicacionInicial != null) 17f else 12f
        )
    }

    LaunchedEffect(Unit) {
        if (ubicacionInicial == null && permisoUbicacion) {
            val actual = ubicacionProvider.obtener()
            if (actual != null) {
                cameraState.animate(
                    CameraUpdateFactory.newLatLngZoom(LatLng(actual.latitude, actual.longitude), 17f)
                )
            }
        }
    }

    Dialog(
        onDismissRequest = onCancelar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onCancelar) {
                        Text(stringResource(R.string.picker_cancel))
                    }

                    Text(
                        text = stringResource(R.string.picker_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    cameraPositionState = cameraState,
                    properties = MapProperties(isMyLocationEnabled = permisoUbicacion),
                    onMapClick = { punto -> marcada = punto }
                ) {
                    marcada?.let { punto ->
                        Marker(state = remember(punto) { MarkerState(position = punto) })
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = marcada?.let {
                            stringResource(
                                R.string.picker_marked,
                                formatearCoordenadas(it.latitude, it.longitude)
                            )
                        } ?: stringResource(R.string.picker_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = { marcada?.let(onConfirmar) },
                        enabled = marcada != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .height(52.dp)
                    ) {
                        Text(stringResource(R.string.picker_confirm))
                    }
                }
            }
        }
    }
}
