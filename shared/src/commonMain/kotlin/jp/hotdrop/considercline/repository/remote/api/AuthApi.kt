package jp.hotdrop.considercline.repository.remote.api

import jp.hotdrop.considercline.repository.remote.KtorHttpClient
import jp.hotdrop.considercline.repository.remote.models.RefreshTokenRequest
import jp.hotdrop.considercline.repository.remote.models.RefreshTokenResponse

class AuthApi(
    private val httpClient: KtorHttpClient
) {
    suspend fun refreshToken(refreshToken: String): RefreshTokenResponse {
        return httpClient.post(
            endpoint = "/auth/refresh",
            body = RefreshTokenRequest(refreshToken)
        )
    }
}
