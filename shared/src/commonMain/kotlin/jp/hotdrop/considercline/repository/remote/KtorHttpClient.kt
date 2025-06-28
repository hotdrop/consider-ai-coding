package jp.hotdrop.considercline.repository.remote

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import jp.hotdrop.considercline.repository.remote.models.Request
import io.ktor.client.HttpClient as KtorNativeClient

class KtorHttpClient(
    private val client: KtorNativeClient
) : HttpClient {
    override suspend fun get(endpoint: String, request: Request?): Map<String, Any?> {
        return client.get(endpoint) {
            request?.urlParam()?.forEach { (key, value) ->
                if (value != null) parameter(key, value)
            }
        }.body()
    }

    override suspend fun post(endpoint: String, request: Request): Map<String, Any?> {
        return client.post(endpoint) {
            setBody(body)
        }.body()
    }
}
