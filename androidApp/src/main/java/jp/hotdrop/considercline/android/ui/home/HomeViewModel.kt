package jp.hotdrop.considercline.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.android.ui.BaseViewModel
import jp.hotdrop.considercline.di.KmpFactory
import jp.hotdrop.considercline.model.AppError
import jp.hotdrop.considercline.model.User
import jp.hotdrop.considercline.model.PointHistory
import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.model.flatMap
import jp.hotdrop.considercline.model.fold
import jp.hotdrop.considercline.model.map
import jp.hotdrop.considercline.usecase.UserUseCase
import jp.hotdrop.considercline.usecase.HistoryUseCase
import jp.hotdrop.considercline.usecase.PointUseCase
import kotlinx.coroutines.ensureActive
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {
    private val userUseCase: UserUseCase by lazy { KmpFactory.useCaseFactory.userUseCase }
    private val pointUseCase: PointUseCase by lazy { KmpFactory.useCaseFactory.pointUseCase }
    private val historyUseCase: HistoryUseCase by lazy { KmpFactory.useCaseFactory.historyUseCase }

    private val mutableUiState = MutableLiveData<HomeUiState>()
    val uiStateLiveData: LiveData<HomeUiState> = mutableUiState

    private val mutableError = MutableLiveData<AppError>()
    val errorLiveData: LiveData<AppError> = mutableError

    fun onLoadAllData() {
        launch {
            dispatcherIO { userUseCase.find() }
                .flatMap { user ->
                    dispatcherIO { pointUseCase.find() }.map { point -> user to point }
                }
                .flatMap { (user, point) ->
                    dispatcherIO { historyUseCase.findAll() }.map {  histories ->
                        HomeUiState(
                            user = user,
                            currentPoint = point,
                            histories = histories
                        )
                    }
                }.also {
                    // ここでキャンセルなら以降は一切実行されない
                    ensureActive()
                }.fold(
                    onSuccess = { mutableUiState.postValue(it) },
                    onFailure = { mutableError.postValue(it) }
                )
        }
    }
}

data class HomeUiState(
    val currentPoint: Point? = null,
    val user: User,
    val histories: List<PointHistory>? = null,
)