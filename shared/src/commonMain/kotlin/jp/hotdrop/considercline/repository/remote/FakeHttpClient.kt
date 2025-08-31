package jp.hotdrop.considercline.repository.remote

import jp.hotdrop.considercline.model.ProgramException
import jp.hotdrop.considercline.model.User
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import jp.hotdrop.considercline.repository.remote.models.PointResponse
import jp.hotdrop.considercline.repository.remote.models.PostPointRequest
import jp.hotdrop.considercline.repository.remote.models.UserResponse
import kotlinx.coroutines.delay
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class FakeHttpClient(
    val sharedPrefs: KmpSharedPreferences
) {
    companion object {
        const val FAKE_COFFEE_USER_ID = "4d58da01395bcaf9"
    }

    suspend fun get(endpoint: String, queryParams: Map<String, String>? = null): Any {
        // 通信しているような雰囲気を出すため遅延を入れる
        delay(2000)

        return when (endpoint) {
            "/user/$FAKE_COFFEE_USER_ID" -> {
                User(
                    userId = FAKE_COFFEE_USER_ID,
                    nickName = "テストユーザー",
                    email = "test@example.com"
                )
            }
            "/point" -> {
                PointResponse(
                    point = sharedPrefs.getPoint()
                )
            }
            else -> throw ProgramException("未実装のエンドポイントです: $endpoint")
        }
    }

    suspend inline fun <reified T> post(endpoint: String, body: Any): T {
        // 通信しているような雰囲気を出すため遅延を入れる
        delay(2000)

        @OptIn(ExperimentalUuidApi::class)
        return when (endpoint) {
            "/user" -> {
                val dummyJwt = Uuid.random().toString()
                val dummyRefreshToken = Uuid.random().toString()
                UserResponse(
                    userId = FAKE_COFFEE_USER_ID,
                    jwt = dummyJwt,
                    refreshToken = dummyRefreshToken
                ) as T
            }
            "/point" -> {
                val request = body as? PostPointRequest ?: throw ProgramException("ポイントの値が不正です")
                val currentPoint = sharedPrefs.getPoint()
                sharedPrefs.savePoint(currentPoint + request.point)
                request.point as T
            }
            "/point/use" -> {
                val request = body as? PostPointRequest ?: throw ProgramException("ポイントの値が不正です")
                val currentPoint = sharedPrefs.getPoint()
                sharedPrefs.savePoint(currentPoint - request.point)
                request.point as T
            }
            "/auth/refresh" -> {
                TODO("リフレッシュトークンが未実装です")
            }
            else -> throw ProgramException("未実装のエンドポイントです: $endpoint")
        }
    }
}
