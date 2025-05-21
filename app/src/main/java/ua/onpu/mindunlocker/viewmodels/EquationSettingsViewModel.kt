package ua.onpu.mindunlocker.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.AndroidViewModel
import ua.onpu.mindunlocker.data.EquationSettings
import ua.onpu.mindunlocker.enums.Topic

class EquationSettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("eq_settings", Context.MODE_PRIVATE)

    private val _settingsMap = mutableStateMapOf<Topic, EquationSettings>()
    val settingsMap: Map<Topic, EquationSettings> = _settingsMap

    private val _enabledTopics = mutableStateListOf<Topic>()
    val enabledTopics: List<Topic> = _enabledTopics

    init {
        Topic.entries.forEach { topic ->
            _settingsMap[topic] = loadSettingsForTopic(topic)
        }
        val saved = prefs.getStringSet("enabled_topics", null)
        if (saved != null) {
            _enabledTopics.addAll(saved.mapNotNull { name -> Topic.entries.find { it.name == name } })
        }
    }

    fun toggleTopicEnabled(topic: Topic) {
        if (_enabledTopics.contains(topic)) {
            _enabledTopics.remove(topic)
        } else {
            _enabledTopics.add(topic)
        }
        prefs.edit()
            .putStringSet("enabled_topics", _enabledTopics.map { it.name }.toSet())
            .apply()
    }

    private fun loadSettingsForTopic(topic: Topic): EquationSettings {
        val key = topic.name
        return EquationSettings(
            minNumber = prefs.getFloat("${key}_min", 1f),
            maxNumber = prefs.getFloat("${key}_max", 10f),
            maxDecimals = prefs.getInt("${key}_decimals", 0),
            allowedOperations = prefs.getStringSet("${key}_ops", topic.defaultOperations.map { it.toString() }.toSet())
                ?.mapNotNull { it.firstOrNull() }?.toSet() ?: topic.defaultOperations
        )
    }

    fun updateSettings(topic: Topic, newSettings: EquationSettings) {
        _settingsMap[topic] = newSettings
        saveSettings(topic, newSettings)
    }

    private fun saveSettings(topic: Topic, settings: EquationSettings) {
        prefs.edit()
            .putFloat("${topic.name}_min", settings.minNumber)
            .putFloat("${topic.name}_max", settings.maxNumber)
            .putInt("${topic.name}_decimals", settings.maxDecimals)
            .putStringSet("${topic.name}_ops", settings.allowedOperations.map { it.toString() }.toSet())
            .apply()
    }
}
