package ua.onpu.mindunlocker.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ua.onpu.mindunlocker.data.EquationSettings

class EquationSettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("equation_settings", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(
        EquationSettings(
            minNumber = prefs.getFloat("minNumber", 1f),
            maxNumber = prefs.getFloat("maxNumber", 20f),
            maxDecimals = prefs.getInt("maxDecimals", 0),
            allowedOperations = prefs.getString("allowedOps", "+-*/")!!.toSet()
        )
    )
    val settings: StateFlow<EquationSettings> = _settings

    fun updateMin(value: Float) {
        _settings.value = _settings.value.copy(minNumber = value)
        prefs.edit().putFloat("minNumber", value).apply()
    }

    fun updateMax(value: Float) {
        _settings.value = _settings.value.copy(maxNumber = value)
        prefs.edit().putFloat("maxNumber", value).apply()
    }

    fun updateDecimals(value: Int) {
        _settings.value = _settings.value.copy(maxDecimals = value)
        prefs.edit().putInt("maxDecimals", value).apply()
    }

    fun toggleOperation(op: Char) {
        val currentOps = _settings.value.allowedOperations.toMutableSet()
        if (currentOps.contains(op)) currentOps.remove(op) else currentOps.add(op)
        _settings.value = _settings.value.copy(allowedOperations = currentOps)
        prefs.edit().putString("allowedOps", currentOps.joinToString("")).apply()
    }
}
