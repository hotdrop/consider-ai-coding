package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.AppComplete
import jp.hotdrop.considercline.model.AppError
import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.model.User
import jp.hotdrop.considercline.repository.UserRepository
import kotlin.coroutines.cancellation.CancellationException

class UserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun find(): AppResult<User> {
        return runCatching {
            userRepository.find()
        }.getOrElse { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }
            AppResult.Error(AppError.UnknownError(throwable))
        }
    }

    suspend fun registerUser(nickname: String?, email: String?): AppComplete {
        return runCatching {
            userRepository.registerUser(nickname, email)
        }.getOrElse { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }
            AppComplete.Error(AppError.UnknownError(throwable))
        }
    }
}