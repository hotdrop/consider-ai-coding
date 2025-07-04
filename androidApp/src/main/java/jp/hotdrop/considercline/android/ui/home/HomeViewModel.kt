package jp.hotdrop.considercline.android.ui.home

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.android.ui.BaseViewModel
import jp.hotdrop.considercline.di.KmpFactory
import jp.hotdrop.considercline.model.AppSetting
import jp.hotdrop.considercline.model.PointHistory
import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.usecase.AppSettingUseCase
import jp.hotdrop.considercline.usecase.HistoryUseCase
import jp.hotdrop.considercline.usecase.PointUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {
    private val appSettingUseCase: AppSettingUseCase by lazy { KmpFactory.useCaseFactory.appSettingUseCase }
    private val pointUseCase: PointUseCase by lazy { KmpFactory.useCaseFactory.pointUseCase }
    private val historyUseCase: HistoryUseCase by lazy { KmpFactory.useCaseFactory.historyUseCase }

    private val mutableUiState = MutableLiveData<HomeUiState>()
    val uiStateLiveData: LiveData<HomeUiState> = mutableUiState

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        launch {
            val uiState = HomeUiState(
                appSetting = appSettingUseCase.find(),
                currentPoint = pointUseCase.find(),
                histories = historyUseCase.findAll()
            )
            mutableUiState.postValue(uiState)
        }
    }

    fun onLoadCurrentPoint() {
        launch {
            val uiState = mutableUiState.value ?: return@launch
            val newUiState = uiState.copyWith(
                currentPoint = pointUseCase.find()
            )
            mutableUiState.postValue(newUiState)
        }
    }

    fun onLoadHistory() {
        launch {
            val uiState = mutableUiState.value ?: return@launch
            val newUiState = uiState.copyWith(
                histories = historyUseCase.findAll()
            )
            mutableUiState.postValue(newUiState)
        }
    }
}

data class HomeUiState(
    val currentPoint: Point? = null,
    val appSetting: AppSetting,
    val histories: List<PointHistory>? = null
) {
    fun copyWith(
        currentPoint: Point? = null,
        appSetting: AppSetting? = null,
        histories: List<PointHistory>? = null
    ): HomeUiState {
        return HomeUiState(
            currentPoint = currentPoint ?: this.currentPoint,
            appSetting = appSetting ?: this.appSetting,
            histories = histories ?: this.histories
        )
    }
}