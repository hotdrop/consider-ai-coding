package jp.hotdrop.considercline.android.ui

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope, DefaultLifecycleObserver {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private val job = SupervisorJob()

    override fun onCleared() {
        super.onCleared()
        // TODO iOSはキャンセルが効かないらしいので何か別の手段が必要
        coroutineContext.cancel()
    }
}