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
            _uiState.update { it.copy(isStartScreenLoading = true) }
            runCatching {
                pointUseCase.find()
            }.onSuccess { currentPoint ->
                _uiState.update { it.copy(currentPoint = currentPoint, isStartScreenLoading = false) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(loadingErrorMessage = throwable.message, isStartScreenLoading = false) }
            }
        }
    }

    fun inputPoint(newInputPoint: Int, maxAvailablePoint: Int) {
        val errorMessage = validateInput(
            input = newInputPoint,
            current = _uiState.value.currentPoint.balance,
            maxAvailablePoint = maxAvailablePoint
        )
        _uiState.update {
            it.copy(
                inputPoint = newInputPoint,
                inputPointErrorMessage = errorMessage,
                isEnableInputPoint = errorMessage == null && newInputPoint > 0
            )
        }
    }

    fun acquirePoint(inputPoint: Int) {
        launch {
            _uiState.update { it.copy(runAcquiringProcess = true) }
            runCatching {
                pointUseCase.acquire(inputPoint)
            }.onSuccess {
                _uiState.update { it.copy(acquireEvent = PointAcquireEvent.ShowSuccessDialog, runAcquiringProcess = false) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(acquireEvent = PointAcquireEvent.ShowErrorDialog(throwable), runAcquiringProcess = false) }
            }
        }
    }

    fun resetAcquireEvent() {
        _uiState.update { it.copy(acquireEvent = null) }
    }

    private fun validateInput(input: Int, current: Int, maxAvailablePoint: Int): String? {
        return when {
            input <= 0 -> "0より大きい値を入力してください。"
            input + current > maxAvailablePoint -> "最大ポイントを超えています。"
            else -> null
        }
    }
}

data class PointGetUiState(
    val currentPoint: Point = Point(0),
    val inputPoint: Int = 0,
    val isStartScreenLoading: Boolean = false,
    val loadingErrorMessage: String? = null,
    val inputPointErrorMessage: String? = null,
    val isEnableInputPoint: Boolean = false,
    val acquireEvent: PointAcquireEvent? = null,
    val runAcquiringProcess: Boolean = false
)

sealed class PointAcquireEvent {
    object ShowSuccessDialog: PointAcquireEvent()
    data class ShowErrorDialog(val throwable: Throwable): PointAcquireEvent()
}