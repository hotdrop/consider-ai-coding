package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.User
import jp.hotdrop.considercline.repository.UserRepository

class UserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun find(): Result<User> {
        return runCatching {
            userRepository.find()
        }
    }

    suspend fun registerUser(nickname: String?, email: String?): Result<Unit> {
        return runCatching {
            userRepository.registerUser(nickname, email)
        }
    }
}