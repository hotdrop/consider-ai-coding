package jp.hotdrop.considercline.android.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hotdrop.considercline.model.AppSetting
import jp.hotdrop.considercline.repository.AppSettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class SplashViewModel(
    private val appSettingRepository: AppSettingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val appSetting = appSettingRepository.find()
                _uiState.value = SplashUiState.Success(appSetting)
            } catch (e: Exception) {
                _uiState.value = SplashUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data class Error(val message: String) : SplashUiState()
    data class Success(val appSetting: AppSetting) : SplashUiState()
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}
