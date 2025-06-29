package jp.hotdrop.considercline.repository.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val userId: String,
    val jwt: String,
    val refreshToken: String
)
