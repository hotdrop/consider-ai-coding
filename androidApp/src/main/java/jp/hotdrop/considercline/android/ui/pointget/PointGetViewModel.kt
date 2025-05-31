package jp.hotdrop.considercline.android.ui.pointget

import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.android.ui.BaseViewModel
import jp.hotdrop.considercline.di.KmpUseCaseFactory
import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.usecase.PointUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PointGetViewModel @Inject constructor() : BaseViewModel() {
    private val pointUseCase: PointUseCase by lazy { KmpUseCaseFactory.pointUseCase }

    private val _currentPoint = MutableStateFlow(Point(0))
    val currentPoint: StateFlow<Point> = _currentPoint.asStateFlow()

    private val _inputPoint = MutableStateFlow("")
    val inputPoint: StateFlow<String> = _inputPoint.asStateFlow()

    private val _showError = MutableStateFlow(false)
    val showError: StateFlow<Boolean> = _showError.asStateFlow()

    val isButtonEnabled: StateFlow<Boolean> = combine(_inputPoint, _showError) { input, error ->
        input.isNotEmpty() && input.toIntOrNull() != null && !error
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    init {
        launch {
            val currentPoint = pointUseCase.find()
            _currentPoint.value = currentPoint
        }
    }

    fun inputPoint(newInput: String, maxAvailablePoint: Int) {
        _inputPoint.value = newInput
        validateInput(newInput, maxAvailablePoint)
    }

    private fun validateInput(input: String, maxAvailablePoint: Int) {
        val point = input.toIntOrNull()
        _showError.value = (point == null || point <= 0 || point > maxAvailablePoint)
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _pointAcquisitionSuccess = MutableSharedFlow<Unit>()
    val pointAcquisitionSuccess: SharedFlow<Unit> = _pointAcquisitionSuccess.asSharedFlow()

    private val _pointAcquisitionError = MutableSharedFlow<Throwable>()
    val pointAcquisitionError: SharedFlow<Throwable> = _pointAcquisitionError.asSharedFlow()

    fun acquirePoint() {
        launch {
            val pointToAcquire = _inputPoint.value.toIntOrNull()

            if (pointToAcquire == null || pointToAcquire <= 0) {
                // isButtonEnabled でバリデーションされているはずだが、念のため
                _pointAcquisitionError.emit(IllegalArgumentException("Invalid point value for acquisition."))
                return@launch
            }

            _isLoading.value = true
            runCatching {
                pointUseCase.acquire(pointToAcquire)
            }.onSuccess { // acquireの戻り値はUnitなので、updatedPoint はない
                // _currentPoint の更新はここでは行わない。
                // 成功イベントを通知し、UI側で後続処理（ダイアログ表示、画面遷移）を行う。
                // ホーム画面に戻った際に最新のポイントが再取得される想定。
                _pointAcquisitionSuccess.emit(Unit)
            }.onFailure { throwable ->
                _pointAcquisitionError.emit(throwable)
            }
            _isLoading.value = false
        }
    }
}
