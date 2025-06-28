package jp.hotdrop.considercline.repository.remote

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.HttpClient as KtorNativeClient

class KtorHttpClient(
    private val client: KtorNativeClient
) : HttpClient {
    override suspend inline fun <reified T> get(endpoint: String, params: Map<String, Any?>): T {
        return client.get(endpoint) {
            params.forEach { (key, value) ->
                if (value != null) parameter(key, value)
            }
        }.body()
    }

    override suspend inline fun <reified T, reified R> post(endpoint: String, body: T): R {
        return client.post(endpoint) {
            setBody(body)
        }.body()
    }
}
