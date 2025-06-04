import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

class UISettingsViewModel : ViewModel() {
    var themeMode = mutableStateOf(ThemeMode.SYSTEM)
}
