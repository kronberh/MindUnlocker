package ua.onpu.mindunlocker.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.onpu.mindunlocker.viewmodels.EquationSettingsViewModel

@Composable
fun EquationSettingsScreen(viewModel: EquationSettingsViewModel) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Number Range", style = MaterialTheme.typography.titleMedium)
        Row {
            TextField(
                value = settings.minNumber.toString(),
                onValueChange = { viewModel.updateMin(it.toFloatOrNull() ?: 0f) },
                label = { Text("Min") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = settings.maxNumber.toString(),
                onValueChange = { viewModel.updateMax(it.toFloatOrNull() ?: 0f) },
                label = { Text("Max") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))
        Text("Max Decimal Places", style = MaterialTheme.typography.titleMedium)
        Slider(
            value = settings.maxDecimals.toFloat(),
            onValueChange = { viewModel.updateDecimals(it.toInt()) },
            valueRange = 0f..5f,
            steps = 4
        )
        Text("${settings.maxDecimals} decimal places")

        Spacer(Modifier.height(16.dp))
        Text("Allowed Operations", style = MaterialTheme.typography.titleMedium)
        Row {
            listOf('+', '-', '*', '/').forEach { op ->
                FilterChip(
                    selected = settings.allowedOperations.contains(op),
                    onClick = { viewModel.toggleOperation(op) },
                    label = { Text(op.toString()) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
