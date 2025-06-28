package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.usecase.AppSettingUseCase
import jp.hotdrop.considercline.usecase.HistoryUseCase
import jp.hotdrop.considercline.usecase.PointUseCase

interface UseCaseFactory {
    val appSettingUseCase: AppSettingUseCase
    val historyUseCase: HistoryUseCase
    val pointUseCase: PointUseCase
}

internal class UseCaseFactoryImpl(
    private val repositoryFactory: RepositoryFactory
) : UseCaseFactory {

    override val appSettingUseCase: AppSettingUseCase by lazy {
        AppSettingUseCase(repositoryFactory.appSettingRepository)
    }
    override val historyUseCase: HistoryUseCase by lazy {
        HistoryUseCase(repositoryFactory.historyRepository)
    }
    override val pointUseCase: PointUseCase by lazy {
        PointUseCase(repositoryFactory.pointRepository, repositoryFactory.historyRepository)
    }
}