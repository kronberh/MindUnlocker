package ua.onpu.mindunlocker

import UISettingsViewModel
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ua.onpu.mindunlocker.enums.Screen
import ua.onpu.mindunlocker.enums.Topic
import ua.onpu.mindunlocker.screens.AppListScreen
import ua.onpu.mindunlocker.screens.HomeScreen
import ua.onpu.mindunlocker.screens.PermissionsScreen
import ua.onpu.mindunlocker.screens.TopicDetailScreen
import ua.onpu.mindunlocker.screens.UISettingsScreen
import ua.onpu.mindunlocker.services.AppLockService
import ua.onpu.mindunlocker.viewmodels.AppsViewModel
import ua.onpu.mindunlocker.viewmodels.EquationSettingsViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val appsViewModel: AppsViewModel = viewModel()
            val uiSettingsViewModel: UISettingsViewModel = viewModel()
            val equationSettingsViewModel: EquationSettingsViewModel = viewModel()
            val context = this
            val lifecycleOwner = LocalLifecycleOwner.current
            val hasUsageStatsPermission = remember { mutableStateOf(false) }

            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        hasUsageStatsPermission.value = checkAccessibilityPermission(context)
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                hasUsageStatsPermission.value = checkAccessibilityPermission(context)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            LaunchedEffect(hasUsageStatsPermission.value) {
                if (hasUsageStatsPermission.value && navController.currentBackStackEntry?.destination?.route == Screen.Permissions.route) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Permissions.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        val currentBackStack by navController.currentBackStackEntryAsState()
                        val currentRoute = currentBackStack?.destination?.route

                        val tabsToShow = if (hasUsageStatsPermission.value) {
                            listOf(Screen.Home, Screen.AppList, Screen.UI)
                        } else {
                            listOf(Screen.Permissions)
                        }

                        tabsToShow.forEach { screen ->
                            NavigationBarItem(
                                selected = currentRoute == screen.route,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                label = { Text(screen.label) },
                                icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                val startDestination =
                    if (hasUsageStatsPermission.value) Screen.Home.route else Screen.Permissions.route

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(equationSettingsViewModel) { topic ->
                            navController.navigate("topic_settings/${topic.name}")
                        }
                    }
                    composable("topic_settings/{topic}") { backStackEntry ->
                        val topicName = backStackEntry.arguments?.getString("topic") ?: return@composable
                        val topic = Topic.valueOf(topicName)
                        TopicDetailScreen(topic, equationSettingsViewModel)
                    }
                    composable(Screen.AppList.route) {
                        AppListScreen(appsViewModel)
                    }
                    composable(Screen.UI.route) {
                        UISettingsScreen(uiSettingsViewModel)
                    }
                    composable(Screen.Permissions.route) {
                        PermissionsScreen()
                    }
                }
            }
        }
    }

    private fun isAccessibilityServiceEnabled(context: Context, serviceId: String): Boolean {
        val accessibilityEnabled = try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            0
        }
        if (accessibilityEnabled == 1) {
            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (!TextUtils.isEmpty(enabledServices)) {
                val colonSplitter = enabledServices.split(":")
                for (service in colonSplitter) {
                    if (service.equals(serviceId, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun checkAccessibilityPermission(context: Context): Boolean {
        val serviceId = "${context.packageName}/${AppLockService::class.java.name}"
        return isAccessibilityServiceEnabled(context, serviceId)
    }
}
