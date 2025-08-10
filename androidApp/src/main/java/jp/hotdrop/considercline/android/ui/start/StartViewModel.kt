package jp.hotdrop.considercline.android.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.android.ui.BaseViewModel
import jp.hotdrop.considercline.di.KmpFactory
import jp.hotdrop.considercline.usecase.UserUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor() : BaseViewModel() {
    private val userUseCase: UserUseCase by lazy { KmpFactory.useCaseFactory.userUseCase }

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

    fun save() {
        _uiState = _uiState.copyWith(isLoading = true)
        mutableUiState.postValue(_uiState)
        launch {
            try {
                userUseCase.registerUser(_uiState.inputNickName, _uiState.inputEmail)
                _uiState = _uiState.copyWith(isComplete = true)
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
    val inputNickName: String = "",
    val inputEmail: String = "",
    val isLoading: Boolean = false,
    val isComplete: Boolean = false
) {
    fun copyWith(
        nickName: String? = null,
        email: String? = null,
        isLoading: Boolean? = null,
        isComplete: Boolean? = null
    ): StartUiState {
        return StartUiState(
            inputNickName = nickName ?: this.inputNickName,
            inputEmail = email ?: this.inputEmail,
            isLoading = isLoading ?: this.isLoading,
            isComplete = isComplete ?: this.isComplete
        )
    }
}