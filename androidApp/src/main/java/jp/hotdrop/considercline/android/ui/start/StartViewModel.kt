package jp.hotdrop.considercline.android.ui.start

import androidx.lifecycle.ViewModel
import jp.hotdrop.considercline.repository.AppSettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

data class StartUiState(
    val nickName: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class StartViewModel(
    private val appSettingRepository: AppSettingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StartUiState())
    val uiState = _uiState.asStateFlow()

    fun onNickNameChanged(newValue: String) {
        _uiState.update { it.copy(nickName = newValue) }
    }

    fun onEmailChanged(newValue: String) {
        _uiState.update { it.copy(email = newValue) }
    }

    fun onErrorDismissed() {
        _uiState.update { it.copy(error = null) }
    }

    suspend fun save() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            appSettingRepository.registerUser(
                _uiState.value.nickName,
                _uiState.value.email
            )
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    companion object {
        val module = module {
            viewModel { StartViewModel(get()) }
        }
    }
}
