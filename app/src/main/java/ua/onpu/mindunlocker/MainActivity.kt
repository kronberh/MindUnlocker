package ua.onpu.mindunlocker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import ua.onpu.mindunlocker.enums.Screen
import ua.onpu.mindunlocker.enums.Topic
import ua.onpu.mindunlocker.screens.AppListScreen
import ua.onpu.mindunlocker.screens.HomeScreen
import ua.onpu.mindunlocker.screens.PermissionsScreen
import ua.onpu.mindunlocker.screens.TopicDetailScreen
import ua.onpu.mindunlocker.viewmodels.AppsViewModel
import ua.onpu.mindunlocker.viewmodels.EquationSettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val appsViewModel: AppsViewModel = viewModel()
            val equationSettingsViewModel: EquationSettingsViewModel = viewModel()

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        val currentBackStack by navController.currentBackStackEntryAsState()
                        val currentRoute = currentBackStack?.destination?.route

                        Screen.entries.forEach { screen ->
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
                NavHost(
                    navController = navController,
                    startDestination = Screen.Permissions.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(viewModel = equationSettingsViewModel) { topic ->
                            navController.navigate("topic_settings/${topic.name}")
                        }
                    }
                    composable("topic_settings/{topic}") { backStackEntry ->
                        val topicName = backStackEntry.arguments?.getString("topic") ?: return@composable
                        val topic = Topic.valueOf(topicName)
                        TopicDetailScreen(topic = topic, viewModel = equationSettingsViewModel)
                    }
                    composable(Screen.AppList.route) {
                        AppListScreen(appsViewModel)
                    }
                    composable(Screen.Permissions.route) {
                        PermissionsScreen()
                    }
                }
            }
        }
    }
}
