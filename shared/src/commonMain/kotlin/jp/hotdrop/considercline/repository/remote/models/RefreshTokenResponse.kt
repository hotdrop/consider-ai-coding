package jp.hotdrop.considercline.repository.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val jwt: String,
    val refreshToken: String
)
