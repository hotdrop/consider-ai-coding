package jp.hotdrop.considercline.android.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.android.ui.BaseViewModel
import jp.hotdrop.considercline.di.KmpUseCaseFactory
import jp.hotdrop.considercline.usecase.AppSettingUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor() : BaseViewModel() {
    private val appSettingUseCase: AppSettingUseCase by lazy { KmpUseCaseFactory.appSettingUseCase }

    private val mutableUiState = MutableLiveData<StartUiState>()
    val uiStateLiveData: LiveData<StartUiState> = mutableUiState

    private val mutableError = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> = mutableError

    private var _uiState = StartUiState()

    fun onNickNameChanged(newValue: String) {
        _uiState = _uiState.copyWith(nickName = newValue)
    }

    fun onEmailChanged(newValue: String) {
        _uiState = _uiState.copyWith(email = newValue)
    }

    fun onErrorDismissed() {
        mutableError.postValue(null)
    }

    fun save() {
        _uiState = _uiState.copyWith(isLoading = true)
        mutableUiState.postValue(_uiState)
        launch {
            try {
                appSettingUseCase.registerUser(_uiState.nickName, _uiState.email)
            } catch (e: Exception) {
                mutableError.postValue(e.message)
            } finally {
                _uiState = _uiState.copyWith(isLoading = false)
                mutableUiState.postValue(_uiState)
            }
        }
    }
}

data class StartUiState(
    val nickName: String = "",
    val email: String = "",
    val isLoading: Boolean = false
) {
    fun copyWith(
        nickName: String? = null,
        email: String? = null,
        isLoading: Boolean? = null
    ): StartUiState {
        return StartUiState(
            nickName = nickName ?: this.nickName,
            email = email ?: this.email,
            isLoading = isLoading ?: this.isLoading
        )
    }
}