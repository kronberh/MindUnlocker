package ua.onpu.mindunlocker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.onpu.mindunlocker.data.EquationSettings
import ua.onpu.mindunlocker.data.generateEquation
import ua.onpu.mindunlocker.enums.Topic

@Composable
fun MathLockScreen(topic: Topic, settings: EquationSettings, onUnlock: () -> Unit) {
    var currentEquation by remember { mutableStateOf(generateEquation(settings)) }
    var isCorrectAnswer by remember { mutableStateOf(currentEquation.answer) }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Topic: " + topic.displayName,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = currentEquation.text,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(onClick = {
                if (isCorrectAnswer) onUnlock()
                else {
                    currentEquation = generateEquation(settings)
                    isCorrectAnswer = currentEquation.answer
                }
            }) {
                Text("True")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                if (!isCorrectAnswer) onUnlock()
                else {
                    currentEquation = generateEquation(settings)
                    isCorrectAnswer = currentEquation.answer
                }
            }) {
                Text("False")
            }
        }
    }
}
