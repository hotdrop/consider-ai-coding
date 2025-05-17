package jp.hotdrop.considercline.android.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import jp.hotdrop.considercline.model.AppSetting
import jp.hotdrop.considercline.repository.AppSettingRepository

class MainViewModel (
    private val appSettingRepository: AppSettingRepository
) : BaseViewModel() {

    private val mutableAppSetting = MutableLiveData<AppSetting>()
    val appSettingLiveData: LiveData<AppSetting> = mutableAppSetting

    private val mutableError = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = mutableError

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        launch {
            try {
                val appSetting = appSettingRepository.find()
                mutableAppSetting.postValue(appSetting)
            } catch (e: Exception) {
                mutableError.postValue(e.message ?: "Unknown error")
            }
        }
    }
}
