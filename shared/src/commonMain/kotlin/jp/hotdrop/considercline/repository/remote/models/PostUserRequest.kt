package jp.hotdrop.considercline.repository.remote.models

import kotlinx.serialization.Serializable

@Serializable
class PostUserRequest(
    val nickname: String?,
    val email: String?
)
