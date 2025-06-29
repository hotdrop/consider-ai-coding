package jp.hotdrop.considercline.di

import jp.hotdrop.considercline.model.HttpClientState
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import jp.hotdrop.considercline.repository.remote.HttpClientFactory
import jp.hotdrop.considercline.repository.remote.KtorHttpClient
import jp.hotdrop.considercline.repository.remote.api.PointApi
import jp.hotdrop.considercline.repository.remote.api.UserApi

internal interface ApiFactory {
    val pointApi: PointApi
    val userApi: UserApi
}

internal class ApiFactoryImpl(
    httpClientState: HttpClientState,
    sharedPreferences: KmpSharedPreferences
): ApiFactory {
    private val httpClient: KtorHttpClient by lazy { HttpClientFactory.create(httpClientState, sharedPreferences) }
    override val pointApi: PointApi by lazy { PointApi(httpClient) }
    override val userApi: UserApi by lazy { UserApi(httpClient) }
}