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

@HiltViewModel
class PointUseViewModel @Inject constructor() : BaseViewModel() {

    private val pointUseCase: PointUseCase by lazy {
        KmpFactory.useCaseFactory.pointUseCase
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadCurrentPoint()
    }

    /**
     * UiStateを更新するためのヘルパー関数。
     * この関数は、UiStateの特定のプロパティのみを更新するために使用されます。
     *
     * @param block 現在のUiStateを受け取り、更新されたUiStateを返すラムダ関数。
     */
    fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update { it.block() }
    }

    private fun loadCurrentPoint() {
        launch {
            val currentPoint = pointUseCase.find()
            _uiState.update {
                it.copy(currentPoint = currentPoint.balance)
            }
        }
    }

    fun inputPoint(newInputPoint: Int) {
        val errorMessage = validateInput(newInputPoint, _uiState.value.currentPoint)
        _uiState.update {
            it.copy(
                inputPoint = newInputPoint,
                errorMessage = errorMessage,
                isEnableInputPoint = errorMessage == null && newInputPoint > 0
            )
        }
    }

    fun usePoint() {
        launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val inputPoint = _uiState.value.inputPoint
                if (validateInput(inputPoint, _uiState.value.currentPoint) == null) {
                    pointUseCase.use(inputPoint)
                    _uiState.update { it.copy(isSuccess = true) }
                } else {
                    _uiState.update { it.copy(errorMessage = "入力されたポイントが不正です。") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "ポイント利用に失敗しました。", isSuccess = false) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
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
    val inputPoint: Int = 0,
    val currentPoint: Int = 0,
    val errorMessage: String? = null,
    val isEnableInputPoint: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)