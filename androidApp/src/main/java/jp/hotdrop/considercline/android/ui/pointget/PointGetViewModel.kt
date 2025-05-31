package jp.hotdrop.considercline.android.ui.pointget

import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.android.ui.BaseViewModel
import jp.hotdrop.considercline.di.KmpUseCaseFactory
import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.usecase.PointUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PointGetViewModel @Inject constructor() : BaseViewModel() {
    private val pointUseCase: PointUseCase by lazy { KmpUseCaseFactory.pointUseCase }

    // TODO 以下はuiStateにした方が良い
    private val _currentPoint = MutableStateFlow(Point(0))
    val currentPoint: StateFlow<Point> = _currentPoint.asStateFlow()

    private val _inputPoint = MutableStateFlow(0)
    val inputPoint: StateFlow<Int> = _inputPoint.asStateFlow()

    private val _showError = MutableStateFlow(false)
    val showError: StateFlow<Boolean> = _showError.asStateFlow()

    val isButtonEnabled: StateFlow<Boolean> = combine(_inputPoint, _showError) { input, error ->
        input > 0 && !error
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    // ポイント獲得処理のUIイベント
    private val _uiEvent = Channel<PointAcquireEvent>(Channel.BUFFERED)
    val uiEventFlow = _uiEvent.receiveAsFlow()

    init {
        launch {
            val currentPoint = pointUseCase.find()
            _currentPoint.value = currentPoint
        }
    }

    fun inputPoint(newInputPoint: Int, maxAvailablePoint: Int) {
        _inputPoint.value = newInputPoint
        validateInput(newInputPoint, maxAvailablePoint)
    }

    private fun validateInput(inputPoint: Int, maxAvailablePoint: Int) {
        _showError.value = (inputPoint <= 0 || inputPoint > maxAvailablePoint)
    }

    fun acquirePoint(inputPoint: Int) {
        launch {
            _uiEvent.send(PointAcquireEvent.NowLoading)
            runCatching {
                pointUseCase.acquire(inputPoint)
            }.onSuccess {
                _uiEvent.send(PointAcquireEvent.ShowSuccessDialog)
            }.onFailure { throwable ->
                _uiEvent.send(PointAcquireEvent.ShowErrorDialog(throwable))
            }
        }
    }
}

sealed class PointAcquireEvent {
    object NowLoading: PointAcquireEvent()
    object ShowSuccessDialog: PointAcquireEvent()
    data class ShowErrorDialog(val throwable: Throwable): PointAcquireEvent()
}