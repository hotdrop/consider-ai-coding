package jp.hotdrop.considercline.repository.remote.models

import jp.hotdrop.considercline.repository.remote.models.Request

class PostPointRequest(
    private val userId: String,
    private val point: Int
) : Request {
    override fun urlParam(): Map<String, Any?>? = null
    override fun body(): Map<String, Any?> = mapOf(
        "userId" to userId,
        "inputPoint" to point
    )
}
