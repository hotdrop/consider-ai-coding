package jp.hotdrop.considercline.repository.remote.models

import jp.hotdrop.considercline.repository.remote.models.Request

class GetPointRequest(
    private val userId: String
) : Request {
    override fun urlParam(): Map<String, Any?> = mapOf("userId" to userId)
    override fun body(): Map<String, Any?>? = null
}
