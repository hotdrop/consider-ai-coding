package jp.hotdrop.considercline.android.ui

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hotdrop.considercline.model.AppError
import jp.hotdrop.considercline.model.AppResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver {
    protected open val handler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is CancellationException) return@CoroutineExceptionHandler
        onUnhandledException(throwable)
    }
    protected open fun onUnhandledException(t: Throwable) { /* no-op or log */ }

    /**
     * このlaunchを使ってsuspend関数を呼んでください。
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(handler) {
            try {
                block()
            } catch (e: CancellationException) {
                throw e
            } catch (t: Throwable) {
                onUnhandledException(t)
            }
        }

    /**
     * I/O 切替（呼び元で withContext を書かない）
     */
    protected suspend fun <T> dispatcherIO(block: suspend () -> T): T =
        withContext(Dispatchers.IO) { block() }
}
