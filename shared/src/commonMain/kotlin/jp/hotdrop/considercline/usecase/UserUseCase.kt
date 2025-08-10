package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.User
import jp.hotdrop.considercline.repository.UserRepository

class UserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun find(): User {
        return userRepository.find()
    }

    suspend fun registerUser(nickname: String?, email: String?) {
        userRepository.registerUser(nickname, email)
    }
}