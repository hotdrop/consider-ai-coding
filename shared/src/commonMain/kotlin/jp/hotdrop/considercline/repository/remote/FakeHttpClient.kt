package jp.hotdrop.considercline.repository.remote

import com.russhwolf.settings.Settings
import jp.hotdrop.considercline.repository.remote.models.Request
import kotlinx.coroutines.delay

class FakeHttpClient(
  private val settings: Settings
) : HttpClient {

    companion object {
        private const val FAKE_COFFEE_USER_ID = "4d58da01395bcaf9"
        private const val FAKE_LOCAL_STORE_POINT_KEY = "key101"
        private const val FAKE_USER_RESPONSE = """
            {
                "id": "$FAKE_COFFEE_USER_ID",
                "name": "テストユーザー",
                "email": "test@example.com"
            }
        """
    }

    override suspend fun get(endpoint: String, request: Request?): Map<String, Any?> {
        // 通信しているような雰囲気を出すため遅延を入れる
        delay(500)

        return when (endpoint) {
            "/user/$FAKE_COFFEE_USER_ID" -> {
                mapOf(
                    "id" to FAKE_COFFEE_USER_ID,
                    "name" to "テストユーザー",
                    "email" to "test@example.com"
                )
            }
            "/point" -> {
                val currentPoint = settings.getInt(FAKE_LOCAL_STORE_POINT_KEY, 0)
                mapOf("point" to currentPoint)
            }
            else -> throw HttpError("未実装のエンドポイントです: $endpoint")
        }
    }

    override suspend fun post(endpoint: String, request: Request): Map<String, Any?> {
        // 通信しているような雰囲気を出すため遅延を入れる
        delay(1000)

        return when (endpoint) {
            "/user" -> {
                mapOf(
                    "id" to FAKE_COFFEE_USER_ID,
                    "name" to "テストユーザー",
                    "email" to "test@example.com"
                )
            }
            "/point" -> {
                val point = request.body()?.get("inputPoint") as? Int
                    ?: throw HttpError("ポイントの値が不正です")
                acquirePoint(point)
                emptyMap()
            }
            "/point/use" -> {
                val point = request.body()?.get("inputPoint") as? Int
                    ?: throw HttpError("ポイントの値が不正です")
                usePoint(point)
                emptyMap()
            }
            else -> throw HttpError("未実装のエンドポイントです: $endpoint")
        }
    }

    private fun acquirePoint(inputPoint: Int) {
        val currentPoint = settings.getInt(FAKE_LOCAL_STORE_POINT_KEY, 0)
        settings.putInt(FAKE_LOCAL_STORE_POINT_KEY, currentPoint + inputPoint)
    }

    private fun usePoint(inputPoint: Int) {
        val currentPoint = settings.getInt(FAKE_LOCAL_STORE_POINT_KEY, 0)
        settings.putInt(FAKE_LOCAL_STORE_POINT_KEY, currentPoint - inputPoint)
    }
}
