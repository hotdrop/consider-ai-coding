package jp.hotdrop.considercline.repository.remote.models

import jp.hotdrop.considercline.model.Point

data class PointResponse(
    val point: Int
) {
    fun toPoint(): Point = Point(point)

    companion object {
        fun mapper(response: Map<String, Any?>): PointResponse {
            return PointResponse(
                point = (response["point"] as? Number)?.toInt() ?: 0
            )
        }
    }
}
