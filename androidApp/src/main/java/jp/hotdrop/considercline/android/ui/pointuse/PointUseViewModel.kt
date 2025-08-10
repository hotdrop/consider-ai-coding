package jp.hotdrop.considercline.android.ui.pointuse

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.hotdrop.considercline.di.KmpFactory
import jp.hotdrop.considercline.usecase.PointUseCase
import jp.hotdrop.considercline.android.ui.BaseViewModel
import jp.hotdrop.considercline.model.AppComplete
import jp.hotdrop.considercline.model.AppError
import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.Point

@HiltViewModel
class PointUseViewModel @Inject constructor() : BaseViewModel() {

    private val pointUseCase: PointUseCase by lazy {
        KmpFactory.useCaseFactory.pointUseCase
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        launch {
            _uiState.update { it.copy(isStartScreenLoading = true) }
            when (val result = pointUseCase.find()) {
                is AppResult.Success -> _uiState.update { it.copy(currentPoint = result.data, isStartScreenLoading = false) }
                is AppResult.Error -> _uiState.update { it.copy(loadingError = result.error, isStartScreenLoading = false) }
            }
        }
    }

    fun inputPoint(newInputPoint: Int) {
        val errorMessage = validateInput(
            input = newInputPoint,
            current = _uiState.value.currentPoint.balance
        )
        _uiState.update {
            it.copy(
                inputPoint = newInputPoint,
                inputPointErrorMessage = errorMessage,
                isEnableInputPoint = errorMessage == null && newInputPoint > 0
            )
        }
    }

    fun usePoint() {
        launch {
            _uiState.update { it.copy(runPointUseProcess = true) }
            val inputPoint = _uiState.value.inputPoint
            when (val result = pointUseCase.use(inputPoint)) {
                AppComplete.Complete -> _uiState.update {
                    it.copy(pointUseEvent = PointUseEvent.ShowSuccessDialog, isStartScreenLoading = false)
                }
                is AppComplete.Error -> _uiState.update {
                    it.copy(pointUseEvent = PointUseEvent.ShowErrorDialog(result.error), isStartScreenLoading = false)
                }
            }
        }
    }

    fun resetGetEvent() {
        _uiState.update { it.copy(pointUseEvent = null) }
    }

    private fun validateInput(input: Int, current: Int): String? {
        return when {
            input <= 0 -> "0より大きい値を入力してください。"
            input > current -> "保有ポイントを超えています。"
            else -> null
        }
    }
}

data class UiState(
    val currentPoint: Point = Point(0),
    val inputPoint: Int = 0,
    val isStartScreenLoading: Boolean = false,
    val loadingError: AppError? = null,
    val inputPointErrorMessage: String? = null,
    val isEnableInputPoint: Boolean = false,
    val pointUseEvent: PointUseEvent? = null,
    val runPointUseProcess: Boolean = false
)

sealed class PointUseEvent {
    object ShowSuccessDialog: PointUseEvent()
    data class ShowErrorDialog(val error: AppError): PointUseEvent()
}