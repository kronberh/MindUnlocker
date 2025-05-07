package ua.onpu.mindunlocker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.onpu.mindunlocker.global.LockState
import ua.onpu.mindunlocker.screens.MathLockScreen
import ua.onpu.mindunlocker.viewmodels.EquationSettingsViewModel

class LockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageNameToUnlock = intent.getStringExtra("PACKAGE_NAME")

        setContent {
            val settingsViewModel: EquationSettingsViewModel = viewModel()
            val settings = settingsViewModel.settings.collectAsState().value

            MaterialTheme {
                MathLockScreen(
                    settings = settings,
                    onUnlock = {
                        LockState.lastUnlockedPackage = packageNameToUnlock
                        finish()
                    }
                )
            }
        }
    }
}
