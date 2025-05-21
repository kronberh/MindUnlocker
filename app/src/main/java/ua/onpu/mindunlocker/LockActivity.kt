package ua.onpu.mindunlocker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.onpu.mindunlocker.data.EquationSettings
import ua.onpu.mindunlocker.enums.Topic
import ua.onpu.mindunlocker.global.LockState
import ua.onpu.mindunlocker.screens.MathLockScreen
import ua.onpu.mindunlocker.viewmodels.EquationSettingsViewModel

class LockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageNameToUnlock = intent.getStringExtra("PACKAGE_NAME")

        setContent {
            val settingsViewModel: EquationSettingsViewModel = viewModel()

            val randomTopic = settingsViewModel.enabledTopics.randomOrNull() ?: Topic.ALGEBRA

            MaterialTheme {
                MathLockScreen(
                    topic = randomTopic,
                    settings = settingsViewModel.settingsMap[randomTopic] ?: EquationSettings(allowedOperations = setOf('+', '-')),
                    onUnlock = {
                        LockState.lastUnlockedPackage = packageNameToUnlock
                        finish()
                    }
                )
            }
        }
    }
}
