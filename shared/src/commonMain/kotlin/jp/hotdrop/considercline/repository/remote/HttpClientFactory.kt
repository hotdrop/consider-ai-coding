package jp.hotdrop.considercline.repository.remote

import io.ktor.client.HttpClient as KtorNativeClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import jp.hotdrop.considercline.model.HttpClientState
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import kotlinx.serialization.json.Json

object HttpClientFactory {
    /**
     * KtorのHttpClientEngineはCIOを使用。よりプラットフォームに適したエンジン (Android: OkHttp, iOS: Darwin) を
     * expect/actual やネイティブからの注入で設定することも可能
     */
    fun create(state: HttpClientState, sharedPrefs: KmpSharedPreferences) : HttpClient {
        return when (state) {
            HttpClientState.useFakeHttp -> FakeHttpClient(sharedPrefs)
            is HttpClientState.useHttpNoneLog -> KtorHttpClient(createNativeClient(apiEntryPoint = state.endpoint, isDebug = false))
            is HttpClientState.useHttpWithDebugLog -> KtorHttpClient(createNativeClient(apiEntryPoint = state.endpoint, isDebug = true))
        }
    }

    private fun createNativeClient(apiEntryPoint: String, isDebug: Boolean): KtorNativeClient {
        return KtorNativeClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = if (isDebug) LogLevel.ALL else LogLevel.NONE
            }

            // デフォルトリクエスト設定
            defaultRequest {
                url(apiEntryPoint)
                contentType(ContentType.Application.Json)
            }
            // 必要に応じてタイムアウト設定などを追加
            // install(HttpTimeout) {
            //     requestTimeoutMillis = 15000L
            //     connectTimeoutMillis = 15000L
            //     socketTimeoutMillis = 15000L
            // }

            // TODO Interceptor(Plugin)を設定する
        }
    }
}