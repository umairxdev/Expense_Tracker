package com.expensetracker.app.presentation.settings

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.core.utils.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val appVersion: String = "1.0.0",
    val isExporting: Boolean = false,
    val isImporting: Boolean = false,
    val showBackupResult: Boolean = false,
    val backupMessage: String = ""
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val backupManager: BackupManager
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun dismissBackupResult() {
        _uiState.value = _uiState.value.copy(showBackupResult = false)
    }

    fun exportToUri(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isExporting = true)
            try {
                backupManager.exportToUri(uri)
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    showBackupResult = true,
                    backupMessage = "Backup exported successfully!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    showBackupResult = true,
                    backupMessage = "Export failed: ${e.message}"
                )
            }
        }
    }

    fun importFromUri(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isImporting = true)
            try {
                val result = backupManager.importFromUri(uri)
                result.fold(
                    onSuccess = { summary ->
                        _uiState.value = _uiState.value.copy(
                            isImporting = false,
                            showBackupResult = true,
                            backupMessage = summary
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isImporting = false,
                            showBackupResult = true,
                            backupMessage = "Import failed: ${error.message}"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isImporting = false,
                    showBackupResult = true,
                    backupMessage = "Import failed: ${e.message}"
                )
            }
        }
    }

    fun shareBackup() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isExporting = true)
            try {
                val uri = backupManager.createLocalBackup()
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/json"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                getApplication<Application>().startActivity(
                    Intent.createChooser(shareIntent, "Share Backup")
                )
                _uiState.value = _uiState.value.copy(isExporting = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    showBackupResult = true,
                    backupMessage = "Share failed: ${e.message}"
                )
            }
        }
    }
}
