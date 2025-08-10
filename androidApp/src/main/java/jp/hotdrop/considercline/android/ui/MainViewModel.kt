package jp.hotdrop.considercline.android.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.di.KmpFactory
import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.User
import jp.hotdrop.considercline.usecase.UserUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {
    private val userUseCase: UserUseCase by lazy { KmpFactory.useCaseFactory.userUseCase }

    private val mutableUser = MutableLiveData<User>()
    val userLiveData: LiveData<User> = mutableUser

    private val mutableError = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = mutableError

    init {
        launch {
            when(val result = dispatcherIO { userUseCase.find() }) {
                is AppResult.Success -> mutableUser.postValue(result.data)
                is AppResult.Error -> mutableError.postValue(result.error.message)
            }
        }
    }
}
