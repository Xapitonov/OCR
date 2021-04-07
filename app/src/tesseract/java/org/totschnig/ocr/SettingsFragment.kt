package org.totschnig.ocr

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference

class SettingsFragment: BaseSettingsFragment(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onResume() {
        super.onResume()
        viewModel.preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        viewModel.preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "tesseract_language" && !viewModel.tessDataExists(requireContext())) {
            viewModel.downloadTessData(requireContext())
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        if (rootKey == null) {
            addPreferencesFromResource(R.xml.engine_preferences)
            val languages = viewModel.getLanguages(requireContext())
            findPreference<ListPreference>("tesseract_language")?.let { preference ->
                preference.entries = languages.map { it.second }.toTypedArray()
                preference.entryValues = languages.map { it.first }.toTypedArray()
            }
        }
    }
}