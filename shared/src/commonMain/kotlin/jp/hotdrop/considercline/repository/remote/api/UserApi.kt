package jp.hotdrop.considercline.repository.remote.api

import jp.hotdrop.considercline.repository.remote.KtorHttpClient
import jp.hotdrop.considercline.repository.remote.models.PostUserRequest
import jp.hotdrop.considercline.repository.remote.models.UserResponse

class UserApi(
    private val httpClient: KtorHttpClient
) {
    /**
     * ユーザー作成
     */
    suspend fun create(nickname: String?, email: String?): UserResponse {
        return httpClient.post(
            endpoint = "/user",
            body = PostUserRequest(nickname, email)
        )
    }
}
