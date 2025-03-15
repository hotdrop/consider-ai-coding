package jp.hotdrop.considercline.repository.remote.models

data class UserResponse(
    val userId: String
) {
    companion object {
        fun mapper(response: Map<String, Any?>): UserResponse {
            return UserResponse(
                userId = (response["userId"] as? String) ?: ""
            )
        }
    }
}
