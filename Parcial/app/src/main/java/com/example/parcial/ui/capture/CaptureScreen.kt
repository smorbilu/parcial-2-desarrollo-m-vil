package com.example.parcial.ui.capture

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parcial.R
import com.example.parcial.data.UbicacionProvider
import com.example.parcial.ui.components.formatearCoordenadas

@Composable
fun CaptureScreen(
    onVolverClic: () -> Unit,
    modifier: Modifier = Modifier,
    captureViewModel: CaptureViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by captureViewModel.state.collectAsState()
    val ubicacionProvider = remember { UbicacionProvider(context.applicationContext) }

    val permisos = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun tienePermisoUbicacion() = permisos.any { permiso ->
        ContextCompat.checkSelfPermission(context, permiso) == PackageManager.PERMISSION_GRANTED
    }

    var permisoUbicacion by remember { mutableStateOf(tienePermisoUbicacion()) }
    var mostrarSelector by remember { mutableStateOf(false) }

    val permisosLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        permisoUbicacion = resultado.values.any { it }
        if (permisoUbicacion) {
            captureViewModel.onCapturarClic()
        } else {
            captureViewModel.onPermisoDenegado()
        }
    }

    val selectorLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        permisoUbicacion = resultado.values.any { it }
        mostrarSelector = true
    }

    if (mostrarSelector) {
        SelectorUbicacionDialog(
            ubicacionInicial = state.ubicacionMarcada,
            ubicacionProvider = ubicacionProvider,
            permisoUbicacion = permisoUbicacion,
            onConfirmar = { punto ->
                captureViewModel.onUbicacionMarcada(punto)
                mostrarSelector = false
            },
            onCancelar = { mostrarSelector = false }
        )
    }

    fun abrirSelector() {
        if (tienePermisoUbicacion()) {
            permisoUbicacion = true
            mostrarSelector = true
        } else {
            selectorLauncher.launch(permisos)
        }
    }

    fun capturar() {
        if (!captureViewModel.formularioValido()) return

        if (state.ubicacionMarcada != null || tienePermisoUbicacion()) {
            captureViewModel.onCapturarClic()
        } else {
            permisosLauncher.launch(permisos)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onVolverClic) {
            Text(stringResource(R.string.common_back))
        }

        Text(
            text = stringResource(R.string.capture_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = state.nombre,
            onValueChange = captureViewModel::onNombreChange,
            label = { Text(stringResource(R.string.capture_name)) },
            singleLine = true,
            enabled = !state.cargando,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.descripcion,
            onValueChange = captureViewModel::onDescripcionChange,
            label = { Text(stringResource(R.string.capture_description)) },
            minLines = 3,
            enabled = !state.cargando,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = { abrirSelector() },
            enabled = !state.cargando,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(stringResource(R.string.capture_mark_location))
        }

        Text(
            text = state.ubicacionMarcada?.let {
                stringResource(
                    R.string.capture_location_marked,
                    formatearCoordenadas(it.latitude, it.longitude)
                )
            } ?: stringResource(R.string.capture_location_auto),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        if (state.ubicacionMarcada != null) {
            TextButton(
                onClick = captureViewModel::onQuitarUbicacion,
                enabled = !state.cargando,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.capture_use_current))
            }
        }

        Button(
            onClick = { capturar() },
            enabled = !state.cargando,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (state.cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(R.string.capture_button))
            }
        }

        if (state.cargando) {
            Text(
                text = stringResource(R.string.capture_loading),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        state.mensajeRes?.let { mensajeRes ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.guardadoExito) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    },
                    contentColor = if (state.guardadoExito) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    }
                )
            ) {
                Text(
                    text = stringResource(mensajeRes, state.mensajeDetalle),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
