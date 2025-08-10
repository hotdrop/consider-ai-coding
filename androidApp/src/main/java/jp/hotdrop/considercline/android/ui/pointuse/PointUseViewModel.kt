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
            pointUseCase.find()
                .onSuccess { point ->
                    _uiState.update { it.copy(currentPoint = point, isStartScreenLoading = false) }
                }.onFailure { throwable ->
                    _uiState.update { it.copy(loadingErrorMessage = throwable.message, isStartScreenLoading = false) }
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
            pointUseCase.use(inputPoint)
                .onSuccess {
                    _uiState.update { it.copy(pointUseEvent = PointUseEvent.ShowSuccessDialog, isStartScreenLoading = false) }
                }.onFailure { throwable ->
                    _uiState.update { it.copy(pointUseEvent = PointUseEvent.ShowErrorDialog(throwable), isStartScreenLoading = false) }
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
    val loadingErrorMessage: String? = null,
    val inputPointErrorMessage: String? = null,
    val isEnableInputPoint: Boolean = false,
    val pointUseEvent: PointUseEvent? = null,
    val runPointUseProcess: Boolean = false
)

sealed class PointUseEvent {
    object ShowSuccessDialog: PointUseEvent()
    data class ShowErrorDialog(val throwable: Throwable): PointUseEvent()
}