package jp.hotdrop.considercline.android.ui.home

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.android.ui.BaseViewModel
import jp.hotdrop.considercline.di.KmpFactory
import jp.hotdrop.considercline.model.User
import jp.hotdrop.considercline.model.PointHistory
import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.usecase.UserUseCase
import jp.hotdrop.considercline.usecase.HistoryUseCase
import jp.hotdrop.considercline.usecase.PointUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {
    private val userUseCase: UserUseCase by lazy { KmpFactory.useCaseFactory.userUseCase }
    private val pointUseCase: PointUseCase by lazy { KmpFactory.useCaseFactory.pointUseCase }
    private val historyUseCase: HistoryUseCase by lazy { KmpFactory.useCaseFactory.historyUseCase }

    private val mutableUiState = MutableLiveData<HomeUiState>()
    val uiStateLiveData: LiveData<HomeUiState> = mutableUiState

    private val mutableError = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = mutableError

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        onLoadAllData()
    }

    fun onLoadAllData() {
        launch {
            historyUseCase.findAll()
                .onSuccess { histories ->
                    pointUseCase.find()
                        .onSuccess { point ->
                            val uiState = HomeUiState(
                                user = userUseCase.find(),
                                currentPoint = point,
                                histories = histories
                            )
                            mutableUiState.postValue(uiState)
                        }

                }
                .onFailure { throwable ->
                    mutableError.postValue(throwable)
                }
        }
    }
}

data class HomeUiState(
    val currentPoint: Point? = null,
    val user: User,
    val histories: List<PointHistory>? = null,
) {
    fun copyWith(
        currentPoint: Point? = null,
        user: User? = null,
        histories: List<PointHistory>? = null
    ): HomeUiState {
        return HomeUiState(
            currentPoint = currentPoint ?: this.currentPoint,
            user = user ?: this.user,
            histories = histories ?: this.histories
        )
    }
}