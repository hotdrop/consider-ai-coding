package jp.hotdrop.considercline.android.ui.pointget

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.android.ui.BaseViewModel
import jp.hotdrop.considercline.di.KmpUseCaseFactory
import jp.hotdrop.considercline.model.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PointGetInputViewModel @Inject constructor(
    private val kmpUseCaseFactory: KmpUseCaseFactory
) : BaseViewModel() {

    private val _currentPoint = MutableStateFlow(Point(0))
    val currentPoint: StateFlow<Point> = _currentPoint.asStateFlow()

    private val _inputPoint = MutableStateFlow("")
    val inputPoint: StateFlow<String> = _inputPoint.asStateFlow()

    private val _showError = MutableStateFlow(false)
    val showError: StateFlow<Boolean> = _showError.asStateFlow()

    val isButtonEnabled: StateFlow<Boolean> = combine(_inputPoint, _showError) { input, error ->
        input.isNotEmpty() && input.toIntOrNull() != null && !error
    }.asStateFlow()

    init {
        viewModelScope.launch {
            kmpUseCaseFactory.pointUseCase.getCurrentPoint().collect { point ->
                _currentPoint.value = point
            }
        }
    }

    fun onInputPointChanged(newInput: String, maxAvailablePoint: Int) {
        _inputPoint.value = newInput
        validateInput(newInput, maxAvailablePoint)
    }

    private fun validateInput(input: String, maxAvailablePoint: Int) {
        val point = input.toIntOrNull()
        _showError.value = if (point == null || point <= 0 || point > maxAvailablePoint) {
            true
        } else {
            false
        }
    }
}
