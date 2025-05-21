package ua.onpu.mindunlocker.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.onpu.mindunlocker.enums.Topic
import ua.onpu.mindunlocker.viewmodels.EquationSettingsViewModel

@Composable
fun HomeScreen(viewModel: EquationSettingsViewModel, onTopicClick: (Topic) -> Unit) {
    val enabledTopics = viewModel.enabledTopics

    Column(Modifier.padding(16.dp)) {
        Text("Select Topics", style = MaterialTheme.typography.titleLarge)

        Topic.entries.forEach { topic ->
            val isEnabled = enabledTopics.contains(topic)
            val color = if (isEnabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onTopicClick(topic) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isEnabled,
                    onCheckedChange = { viewModel.toggleTopicEnabled(topic) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(topic.displayName, color = color)
            }
        }
    }
}
