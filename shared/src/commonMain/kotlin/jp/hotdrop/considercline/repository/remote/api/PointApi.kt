package jp.hotdrop.considercline.repository.remote.api

import jp.hotdrop.considercline.model.Point
import jp.hotdrop.considercline.repository.remote.HttpClient
import jp.hotdrop.considercline.repository.remote.models.GetPointRequest
import jp.hotdrop.considercline.repository.remote.models.PointResponse
import jp.hotdrop.considercline.repository.remote.models.PostPointRequest

class PointApi(
    private val httpClient: HttpClient
) {
    /**
     * ユーザーの保有ポイント取得
     */
    suspend fun find(userId: String): Point {
        val response = httpClient.get(
            endpoint = "/point",
            request = GetPointRequest(userId)
        )
        return PointResponse.mapper(response).toPoint()
    }

    /**
     * ポイント獲得
     */
    suspend fun acquired(userId: String, inputPoint: Int) {
        httpClient.post(
            endpoint = "/point",
            request = PostPointRequest(userId, inputPoint)
        )
    }

    /**
     * ポイント利用
     */
    suspend fun use(userId: String, inputPoint: Int) {
        httpClient.post(
            endpoint = "/point/use",
            request = PostPointRequest(userId, inputPoint)
        )
    }
}
