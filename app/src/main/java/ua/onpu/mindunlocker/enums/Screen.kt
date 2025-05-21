package ua.onpu.mindunlocker.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Security
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val route: String, val label: String, val icon: ImageVector) {
    Home("home", "Home", Icons.Filled.Home),
    AppList("appList", "Apps", Icons.Filled.Apps),
    Permissions("permissions", "Security", Icons.Filled.Security)
}
