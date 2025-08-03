package jp.hotdrop.considercline.android.ui.home

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.android.ui.BaseViewModel
import jp.hotdrop.considercline.di.KmpFactory
import jp.hotdrop.considercline.model.AppResult
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
        onLoadAllData()
    }

    fun onLoadAllData() {
        launch {
            when (val result = historyUseCase.findAll()) {
                is AppResult.Success -> {
                    val uiState = HomeUiState(
                        appSetting = appSettingUseCase.find(),
                        currentPoint = pointUseCase.find(),
                        histories = result.data
                    )
                    mutableUiState.postValue(uiState)
                }
                is AppResult.Error -> { /** TODO エラー処理 */ }
            }
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