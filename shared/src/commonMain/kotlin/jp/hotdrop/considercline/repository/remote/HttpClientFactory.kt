package jp.hotdrop.considercline.repository.remote

import io.ktor.client.HttpClient as KtorNativeClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import jp.hotdrop.considercline.model.HttpClientState
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import jp.hotdrop.considercline.repository.remote.api.AuthApi
import kotlinx.serialization.json.Json

object HttpClientFactory {
    /**
     * KtorのHttpClientEngineはCIOを使用。よりプラットフォームに適したエンジン (Android: OkHttp, iOS: Darwin) を
     * expect/actual やネイティブからの注入で設定することも可能
     */
    fun create(state: HttpClientState, sharedPrefs: KmpSharedPreferences) : KtorHttpClient {
        return when (state) {
            HttpClientState.useFakeHttp -> KtorHttpClient(
                createNativeClient(apiEntryPoint = "", isDebug = true, sharedPrefs = sharedPrefs),
                FakeHttpClient(sharedPrefs = sharedPrefs)
            )
            is HttpClientState.useHttpNoneLog -> KtorHttpClient(
                createNativeClient(apiEntryPoint = state.endpoint, isDebug = false, sharedPrefs = sharedPrefs)
            )
            is HttpClientState.useHttpWithDebugLog -> KtorHttpClient(
                createNativeClient(apiEntryPoint = state.endpoint, isDebug = true, sharedPrefs = sharedPrefs)
            )
        }
    }

    private fun createNativeClient(
        apiEntryPoint: String,
        isDebug: Boolean,
        sharedPrefs: KmpSharedPreferences
    ): KtorNativeClient {
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

            install(Auth) {
                bearer {
                    loadTokens {
                        val jwt = sharedPrefs.getJwt()
                        val refreshToken = sharedPrefs.getRefreshToken()
                        if (jwt != null && refreshToken != null) {
                            BearerTokens(jwt, refreshToken)
                        } else {
                            null
                        }
                    }
                    refreshTokens {
                        val refreshToken = oldTokens?.refreshToken
                        if (refreshToken != null) {
                            val refreshTokenClient = KtorHttpClient(createRefreshTokenClient(apiEntryPoint, isDebug))
                            val authApi = AuthApi(refreshTokenClient)
                            val response = authApi.refreshToken(refreshToken)
                            sharedPrefs.saveJwt(response.jwt)
                            sharedPrefs.saveRefreshToken(response.refreshToken)
                            BearerTokens(response.jwt, response.refreshToken)
                        } else {
                            null
                        }
                    }
                    sendWithoutRequest { request ->
                        // UserApi.createは認証不要なので除外
                        request.url.encodedPath.contains("/user")
                    }
                }
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
        }
    }

    /**
     * トークンリフレッシュ専用のKtorNativeClientを生成する。
     * Authプラグインを含まないため、リフレッシュ処理が再帰的にAuthプラグインをトリガーすることはない。
     */
    private fun createRefreshTokenClient(
        apiEntryPoint: String,
        isDebug: Boolean
    ): KtorNativeClient {
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

            defaultRequest {
                url(apiEntryPoint)
                contentType(ContentType.Application.Json)
            }
        }
    }
}
