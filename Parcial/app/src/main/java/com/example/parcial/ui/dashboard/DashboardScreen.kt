package com.example.parcial.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.parcial.R

@Composable
fun DashboardScreen(
    nombre: String,
    onCapturarClic: () -> Unit,
    onListadoClic: () -> Unit,
    onMapaClic: () -> Unit,
    onCerrarSesionClic: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.dashboard_title, nombre),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.dashboard_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        OpcionDashboard(
            titulo = stringResource(R.string.dashboard_capture_title),
            descripcion = stringResource(R.string.dashboard_capture_desc),
            onClick = onCapturarClic
        )

        OpcionDashboard(
            titulo = stringResource(R.string.dashboard_list_title),
            descripcion = stringResource(R.string.dashboard_list_desc),
            onClick = onListadoClic
        )

        OpcionDashboard(
            titulo = stringResource(R.string.dashboard_map_title),
            descripcion = stringResource(R.string.dashboard_map_desc),
            onClick = onMapaClic
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onCerrarSesionClic,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.dashboard_logout))
        }
    }
}

@Composable
private fun OpcionDashboard(
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = titulo, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = descripcion, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
