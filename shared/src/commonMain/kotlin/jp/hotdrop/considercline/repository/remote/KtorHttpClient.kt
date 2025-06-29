package jp.hotdrop.considercline.repository.remote

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.HttpClient as KtorNativeClient

class KtorHttpClient(
    val client: KtorNativeClient,
    val fakeHttpClient: FakeHttpClient? = null
)  {
    suspend inline fun <reified T> get(endpoint: String, queryParams: Map<String, String>? = null): T {
        if (fakeHttpClient != null) {
            return fakeHttpClient.get(endpoint, queryParams) as T
        }
        return client.get(endpoint) {
            queryParams?.forEach { (key, value) ->
                parameter(key, value)
            }
        }.body()
    }

    suspend inline fun <reified T> post(endpoint: String, queryParams: Map<String, String>? = null, body: Any): T {
        if (fakeHttpClient != null) {
            return fakeHttpClient.post(endpoint, body) as T
        }
        return client.post(endpoint) {
            queryParams?.forEach { (key, value) ->
                parameter(key, value)
            }
            setBody(body)
        }.body()
    }
}
