package jp.hotdrop.considercline.repository.remote.models

import kotlinx.serialization.Serializable

@Serializable
class PostPointRequest(
    val userId: String,
    val point: Int
)
