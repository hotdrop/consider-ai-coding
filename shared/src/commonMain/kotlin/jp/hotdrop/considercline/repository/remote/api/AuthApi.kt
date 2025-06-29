package jp.hotdrop.considercline.repository.remote.api

import jp.hotdrop.considercline.repository.remote.HttpClient
import jp.hotdrop.considercline.repository.remote.models.RefreshTokenRequest
import jp.hotdrop.considercline.repository.remote.models.RefreshTokenResponse

class AuthApi(
    private val httpClient: HttpClient
) {
    suspend fun refreshToken(refreshToken: String): RefreshTokenResponse {
        return httpClient.post(
            endpoint = "/auth/refresh",
            request = RefreshTokenRequest(refreshToken)
        )
    }
}
