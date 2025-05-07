package ua.onpu.mindunlocker.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AppsViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("locked_apps", Context.MODE_PRIVATE)
    private val _lockedApps = mutableStateListOf<String>()
    val lockedApps: List<String> = _lockedApps

    init {
        val saved = prefs.getStringSet("locked", emptySet()) ?: emptySet()
        _lockedApps.addAll(saved)
    }

    fun toggleAppLock(packageName: String) {
        viewModelScope.launch {
            if (_lockedApps.contains(packageName)) {
                _lockedApps.remove(packageName)
            } else {
                _lockedApps.add(packageName)
            }
            prefs.edit().putStringSet("locked", _lockedApps.toSet()).apply()
        }
    }
}
