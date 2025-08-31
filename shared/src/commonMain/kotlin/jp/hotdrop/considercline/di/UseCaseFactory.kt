package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.usecase.UserUseCase
import jp.hotdrop.considercline.usecase.HistoryUseCase
import jp.hotdrop.considercline.usecase.PointUseCase

interface UseCaseFactory {
    val userUseCase: UserUseCase
    val historyUseCase: HistoryUseCase
    val pointUseCase: PointUseCase
}

internal class UseCaseFactoryImpl(
    private val repositoryFactory: RepositoryFactory
) : UseCaseFactory {

    override val userUseCase: UserUseCase by lazy {
        UserUseCase(repositoryFactory.userRepository)
    }
    override val historyUseCase: HistoryUseCase by lazy {
        HistoryUseCase(repositoryFactory.historyRepository)
    }
    override val pointUseCase: PointUseCase by lazy {
        PointUseCase(repositoryFactory.pointRepository, repositoryFactory.historyRepository)
    }
}