package com.expensetracker.app.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.local.entity.CategoryEntity
import com.expensetracker.app.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryItem(
    val name: String,
    val displayName: String,
    val isExpense: Boolean,
    val isDefault: Boolean
)

data class CategoriesUiState(
    val categories: List<CategoryItem> = emptyList(),
    val showAddDialog: Boolean = false,
    val addName: String = "",
    val addDisplayName: String = "",
    val addType: String = "EXPENSE",
    val deleteConfirm: CategoryItem? = null
)

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.seedDefaults()
            categoryRepository.getAll().collect { entities ->
                _uiState.value = _uiState.value.copy(
                    categories = entities.map {
                        CategoryItem(
                            name = it.name,
                            displayName = it.displayName,
                            isExpense = it.type == "EXPENSE",
                            isDefault = it.isDefault
                        )
                    }
                )
            }
        }
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = true, addName = "", addDisplayName = "", addType = "EXPENSE")
    }

    fun hideAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false)
    }

    fun setAddName(name: String) {
        _uiState.value = _uiState.value.copy(addName = name)
    }

    fun setAddDisplayName(displayName: String) {
        _uiState.value = _uiState.value.copy(addDisplayName = displayName)
    }

    fun setAddType(type: String) {
        _uiState.value = _uiState.value.copy(addType = type)
    }

    fun addCategory() {
        val state = _uiState.value
        val name = state.addName.uppercase().replace(" ", "_")
        val displayName = state.addDisplayName.ifEmpty { state.addName }
        if (name.isBlank()) return
        viewModelScope.launch {
            categoryRepository.addCategory(name, displayName, state.addType)
            _uiState.value = _uiState.value.copy(showAddDialog = false)
        }
    }

    fun requestDelete(item: CategoryItem) {
        _uiState.value = _uiState.value.copy(deleteConfirm = item)
    }

    fun cancelDelete() {
        _uiState.value = _uiState.value.copy(deleteConfirm = null)
    }

    fun confirmDelete() {
        val item = _uiState.value.deleteConfirm ?: return
        viewModelScope.launch {
            categoryRepository.deleteCategory(item.name)
            _uiState.value = _uiState.value.copy(deleteConfirm = null)
        }
    }
}
