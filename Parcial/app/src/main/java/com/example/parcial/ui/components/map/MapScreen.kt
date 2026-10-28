package com.example.parcial.ui.components.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parcial.R
import com.example.parcial.model.response.UbicacionDto
import com.example.parcial.ui.components.MapaUbicaciones
import com.example.parcial.ui.components.formatearCoordenadas
import com.example.parcial.ui.components.formatearFecha

@Composable
fun MapScreen(
    onVolverClic: () -> Unit,
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = viewModel()
) {
    val state by mapViewModel.state.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TextButton(
            onClick = onVolverClic,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(stringResource(R.string.common_back))
        }

        Text(
            text = stringResource(R.string.map_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            MapaUbicaciones(
                ubicaciones = state.ubicaciones,
                onMarcadorClic = mapViewModel::onMarcadorClic
            )

            when {
                state.cargando -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                state.mensajeRes != null -> MensajeFlotante(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = stringResource(state.mensajeRes!!, state.mensajeDetalle),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = mapViewModel::cargarUbicaciones) {
                        Text(stringResource(R.string.locations_retry))
                    }
                }

                state.cargadas && state.ubicaciones.isEmpty() -> MensajeFlotante(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = stringResource(R.string.map_empty),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    val seleccionada = state.ubicacionSeleccionada
    if (state.dialogoVisible && seleccionada != null) {
        DetalleUbicacionDialog(
            ubicacion = seleccionada,
            onCerrar = mapViewModel::cerrarDialogo
        )
    }
}

@Composable
private fun MensajeFlotante(
    modifier: Modifier = Modifier,
    contenido: @Composable () -> Unit
) {
    Card(
        modifier = modifier.padding(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            contenido()
        }
    }
}

@Composable
private fun DetalleUbicacionDialog(
    ubicacion: UbicacionDto,
    onCerrar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(ubicacion.nombre) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                CampoDialogo(
                    etiqueta = stringResource(R.string.map_dialog_description),
                    valor = ubicacion.descripcion.ifBlank {
                        stringResource(R.string.locations_no_description)
                    }
                )
                CampoDialogo(
                    etiqueta = stringResource(R.string.map_dialog_coordinates),
                    valor = formatearCoordenadas(ubicacion.latitud, ubicacion.longitud)
                )
                val fecha = formatearFecha(ubicacion.creadoEn)
                if (fecha.isNotEmpty()) {
                    CampoDialogo(
                        etiqueta = stringResource(R.string.map_dialog_date),
                        valor = fecha
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onCerrar) {
                Text(stringResource(R.string.map_dialog_close))
            }
        }
    )
}

@Composable
private fun CampoDialogo(etiqueta: String, valor: String) {
    Column {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = valor, style = MaterialTheme.typography.bodyMedium)
    }
}
