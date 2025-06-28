package jp.hotdrop.considercline.repository.remote.models

class PostUserRequest(
    private val nickname: String?,
    private val email: String?
) : Request {
    override fun urlParam(): Map<String, Any?>? = null
    override fun body(): Map<String, Any?> = mapOf(
        "nickname" to nickname,
        "email" to email
    )
}
