package ua.onpu.mindunlocker.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ua.onpu.mindunlocker.enums.Topic
import ua.onpu.mindunlocker.viewmodels.EquationSettingsViewModel

@Composable
fun TopicDetailScreen(topic: Topic, viewModel: EquationSettingsViewModel) {
    val settings = viewModel.settingsMap[topic] ?: return

    var minNumberStr by remember { mutableStateOf(settings.minNumber.toString()) }
    var maxNumberStr by remember { mutableStateOf(settings.maxNumber.toString()) }
    var maxDecimals by remember { mutableStateOf(settings.maxDecimals) }
    var allowedOperations by remember { mutableStateOf(settings.allowedOperations) }

    val minNumber = minNumberStr.toFloatOrNull() ?: settings.minNumber
    val maxNumber = maxNumberStr.toFloatOrNull() ?: settings.maxNumber

    LaunchedEffect(minNumber, maxNumber, maxDecimals, allowedOperations) {
        if (minNumber <= maxNumber) {
            viewModel.updateSettings(
                topic,
                settings.copy(
                    minNumber = minNumber,
                    maxNumber = maxNumber,
                    maxDecimals = maxDecimals,
                    allowedOperations = allowedOperations
                )
            )
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Settings for ${topic.displayName}", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = minNumberStr,
            onValueChange = { input ->
                if (input.isEmpty() || input.all { it.isDigit() || it == '.' }) {
                    minNumberStr = input
                }
            },
            label = { Text("Min Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = maxNumberStr,
            onValueChange = { input ->
                if (input.isEmpty() || input.all { it.isDigit() || it == '.' }) {
                    maxNumberStr = input
                }
            },
            label = { Text("Max Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Max Decimals: $maxDecimals")

        Row {
            Slider(
                value = maxDecimals.toFloat(),
                onValueChange = { maxDecimals = it.toInt() },
                valueRange = 0f..5f,
                steps = 5,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Allowed Operations:")

        val allOps = listOf('+', '-', '*', '/')
        Row {
            allOps.forEach { op ->
                val selected = allowedOperations.contains(op)
                Button(
                    onClick = {
                        allowedOperations = if (selected) {
                            allowedOperations - op
                        } else {
                            allowedOperations + op
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f) // Gray background for unselected
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        op.toString(),
                        color = if (selected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
