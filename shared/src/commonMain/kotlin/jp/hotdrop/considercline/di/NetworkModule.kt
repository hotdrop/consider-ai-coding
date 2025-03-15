package jp.hotdrop.considercline.di

import io.ktor.client.HttpClient as KtorClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import jp.hotdrop.considercline.repository.remote.HttpClient
import jp.hotdrop.considercline.repository.remote.KtorHttpClient
import jp.hotdrop.considercline.repository.remote.api.PointApi
import jp.hotdrop.considercline.repository.remote.api.UserApi
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single { 
        KtorClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }
    single<HttpClient> { KtorHttpClient(get()) }
    single { PointApi(get()) }
    single { UserApi(get()) }
}
