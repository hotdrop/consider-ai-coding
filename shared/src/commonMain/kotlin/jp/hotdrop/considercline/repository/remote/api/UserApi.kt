package jp.hotdrop.considercline.repository.remote.api

import jp.hotdrop.considercline.repository.remote.HttpClient
import jp.hotdrop.considercline.repository.remote.models.PostUserRequest
import jp.hotdrop.considercline.repository.remote.models.UserResponse

class UserApi(
    private val httpClient: HttpClient
) {
    /**
     * ユーザー作成
     */
    suspend fun create(nickname: String?, email: String?): String {
        val response = httpClient.post(
            endpoint = "/user",
            request = PostUserRequest(nickname, email)
        )
        return UserResponse.mapper(response).userId
    }
}
