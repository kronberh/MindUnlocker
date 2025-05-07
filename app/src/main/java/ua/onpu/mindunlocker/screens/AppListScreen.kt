package ua.onpu.mindunlocker.screens

import android.content.pm.PackageManager
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import ua.onpu.mindunlocker.viewmodels.AppsViewModel

@Composable
fun AppListScreen(viewModel: AppsViewModel) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val installedApps = remember {
        packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { packageManager.getLaunchIntentForPackage(it.packageName) != null }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(installedApps) { app ->
            val isLocked = viewModel.lockedApps.contains(app.packageName)
            Row(
                Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(app.loadLabel(packageManager).toString())
                Checkbox(
                    checked = isLocked,
                    onCheckedChange = {
                        viewModel.toggleAppLock(app.packageName)
                    }
                )
            }
        }
    }
}
