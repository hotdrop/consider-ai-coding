package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.AppComplete
import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.User
import jp.hotdrop.considercline.model.mapToDomain
import jp.hotdrop.considercline.repository.UserRepository

class UserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun find(): AppResult<User> {
        return fetchUser()
    }

    suspend fun findForIos(): User {
        return when (val result = fetchUser()) {
            is AppResult.Success -> result.data
            is AppResult.Error -> throw mapToDomain(result.error)
        }
    }

    private suspend fun fetchUser(): AppResult<User> {
        return userRepository.find()
    }

    suspend fun registerUser(nickname: String?, email: String?): AppComplete {
        return saveRegisterUser(nickname, email)
    }

    suspend fun registerUserForIos(nickname: String?, email: String?) {
        return when (val result = saveRegisterUser(nickname, email)) {
            is AppComplete.Complete -> Unit
            is AppComplete.Error -> throw mapToDomain(result.error)
        }
    }

    private suspend fun saveRegisterUser(nickname: String?, email: String?): AppComplete {
        return userRepository.registerUser(nickname, email)
    }
}