package jp.hotdrop.considercline.android.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.hotdrop.considercline.di.KmpFactory
import kotlinx.coroutines.launch
import jp.hotdrop.considercline.model.AppSetting
import jp.hotdrop.considercline.usecase.AppSettingUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {
    private val appSettingUseCase: AppSettingUseCase by lazy { KmpFactory.useCaseFactory.appSettingUseCase }

    private val mutableAppSetting = MutableLiveData<AppSetting>()
    val appSettingLiveData: LiveData<AppSetting> = mutableAppSetting

    private val mutableError = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = mutableError

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        launch {
            try {
                val appSetting = appSettingUseCase.find()
                mutableAppSetting.postValue(appSetting)
            } catch (e: Exception) {
                mutableError.postValue(e.message ?: "Unknown error")
            }
        }
    }
}
