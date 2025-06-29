package jp.hotdrop.considercline.repository.remote.models

import kotlinx.serialization.Serializable

@Serializable
class GetPointRequest(
    val userId: String
)