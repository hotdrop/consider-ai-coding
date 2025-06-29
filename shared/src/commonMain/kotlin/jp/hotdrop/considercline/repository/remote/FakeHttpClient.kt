package jp.hotdrop.considercline.repository.remote

import jp.hotdrop.considercline.model.AppSetting
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import jp.hotdrop.considercline.repository.remote.models.PointResponse
import jp.hotdrop.considercline.repository.remote.models.PostUserRequest
import jp.hotdrop.considercline.repository.remote.models.UserResponse
import kotlinx.coroutines.delay

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
                AppSetting(
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
            else -> throw HttpError("未実装のエンドポイントです: $endpoint")
        }
    }

    suspend inline fun <reified T> post(endpoint: String, body: Any): T {
        // 通信しているような雰囲気を出すため遅延を入れる
        delay(2000)

        return when (endpoint) {
            "/user" -> {
                // TODO JWTのダミー値を入れる
                UserResponse(
                    userId = FAKE_COFFEE_USER_ID,
                    jwt = "",
                    refreshToken = ""
                ) as T
            }
            "/point" -> {
                val point = body as? Int ?: throw HttpError("ポイントの値が不正です")
                val currentPoint = sharedPrefs.getPoint()
                sharedPrefs.savePoint(currentPoint + point)
                point as T
            }
            "/point/use" -> {
                val point = body as? Int ?: throw HttpError("ポイントの値が不正です")
                val currentPoint = sharedPrefs.getPoint()
                sharedPrefs.savePoint(currentPoint - point)
                point as T
            }
            else -> throw HttpError("未実装のエンドポイントです: $endpoint")
        }
    }
}
