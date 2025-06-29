package jp.hotdrop.considercline.repository.remote.api

import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.repository.remote.KtorHttpClient
import jp.hotdrop.considercline.repository.remote.models.PointResponse
import jp.hotdrop.considercline.repository.remote.models.PostPointRequest

class PointApi(
    private val httpClient: KtorHttpClient
) {
    /**
     * ユーザーの保有ポイント取得
     */
    suspend fun find(userId: String): Point {
        val response = httpClient.get<PointResponse>(
            endpoint = "/point",
            queryParams = mapOf("userId" to userId),
        )
        return Point(balance = response.point)
    }

    /**
     * ポイント獲得
     */
    suspend fun acquired(userId: String, inputPoint: Int) {
        httpClient.post<Any>(
            endpoint = "/point",
            body = PostPointRequest(userId, inputPoint)
        )
    }

    /**
     * ポイント利用
     */
    suspend fun use(userId: String, inputPoint: Int) {
        httpClient.post<Any>(
            endpoint = "/point/use",
            body = PostPointRequest(userId, inputPoint)
        )
    }
}
