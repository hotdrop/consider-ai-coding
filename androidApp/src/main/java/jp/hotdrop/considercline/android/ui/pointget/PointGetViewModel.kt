package jp.hotdrop.considercline.android.ui.pointget

import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.android.ui.BaseViewModel
import jp.hotdrop.considercline.di.KmpFactory
import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.usecase.PointUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PointGetViewModel @Inject constructor() : BaseViewModel() {
    private val pointUseCase: PointUseCase by lazy { KmpFactory.useCaseFactory.pointUseCase }

    private val _uiState = MutableStateFlow(PointGetUiState())
    val uiState: StateFlow<PointGetUiState> = _uiState.asStateFlow()

    init {
        launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                pointUseCase.find()
            }.onSuccess { currentPoint ->
                _uiState.update { it.copy(currentPoint = currentPoint, isLoading = false) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(errorMessage = throwable.message, isLoading = false) }
            }
        }
    }

    fun inputPoint(newInputPoint: Int, maxAvailablePoint: Int) {
        _uiState.update {
            val showError = (newInputPoint <= 0 || newInputPoint > maxAvailablePoint)
            it.copy(
                inputPoint = newInputPoint,
                showError = showError,
                isButtonEnabled = newInputPoint > 0 && !showError
            )
        }
    }

    fun acquirePoint(inputPoint: Int) {
        launch {
            _uiState.update { it.copy(isAcquiring = true) }
            runCatching {
                pointUseCase.acquire(inputPoint)
            }.onSuccess {
                _uiState.update { it.copy(acquireEvent = PointAcquireEvent.ShowSuccessDialog, isAcquiring = false) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(acquireEvent = PointAcquireEvent.ShowErrorDialog(throwable), isAcquiring = false) }
            }
        }
    }

    fun resetAcquireEvent() {
        _uiState.update { it.copy(acquireEvent = null) }
    }
}

data class PointGetUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentPoint: Point = Point(0),
    val inputPoint: Int = 0,
    val showError: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val acquireEvent: PointAcquireEvent? = null,
    val isAcquiring: Boolean = false
)

sealed class PointAcquireEvent {
    object ShowSuccessDialog: PointAcquireEvent()
    data class ShowErrorDialog(val throwable: Throwable): PointAcquireEvent()
}