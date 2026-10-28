package com.example.parcial.ui.locations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parcial.R
import com.example.parcial.model.response.UbicacionDto
import com.example.parcial.ui.components.formatearCoordenadas
import com.example.parcial.ui.components.formatearFecha

@Composable
fun LocationsScreen(
    onVolverClic: () -> Unit,
    onMapaClic: () -> Unit,
    modifier: Modifier = Modifier,
    locationsViewModel: LocationsViewModel = viewModel()
) {
    val state by locationsViewModel.state.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onVolverClic) {
                Text(stringResource(R.string.common_back))
            }
            TextButton(onClick = onMapaClic) {
                Text(stringResource(R.string.locations_view_map))
            }
        }

        Text(
            text = stringResource(R.string.locations_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when {
                state.cargando -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                state.mensajeRes != null -> Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(state.mensajeRes!!, state.mensajeDetalle),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = locationsViewModel::cargarUbicaciones) {
                        Text(stringResource(R.string.locations_retry))
                    }
                }

                state.cargadas && state.ubicaciones.isEmpty() -> Text(
                    text = stringResource(R.string.locations_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 24.dp,
                        vertical = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.ubicaciones, key = { it.id }) { ubicacion ->
                        UbicacionCard(ubicacion)
                    }
                }
            }
        }
    }
}

@Composable
private fun UbicacionCard(ubicacion: UbicacionDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = ubicacion.nombre, style = MaterialTheme.typography.titleMedium)

            Text(
                text = ubicacion.descripcion.ifBlank { stringResource(R.string.locations_no_description) },
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(
                    R.string.locations_coordinates,
                    formatearCoordenadas(ubicacion.latitud, ubicacion.longitud)
                ),
                style = MaterialTheme.typography.bodySmall
            )

            val fecha = formatearFecha(ubicacion.creadoEn)
            if (fecha.isNotEmpty()) {
                Text(text = fecha, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
