package jp.hotdrop.considercline.repository.remote

import io.ktor.client.HttpClient as KtorClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import jp.hotdrop.considercline.repository.remote.models.Request

class KtorHttpClient(private val client: KtorClient) : HttpClient {

    companion object {
        private const val TIMEOUT_MILLIS = 30_000L
    }

    override suspend fun get(endpoint: String, request: Request?): Map<String, Any?> {
        return try {
            val response = client.get(endpoint) {
                timeout {
                    requestTimeoutMillis = TIMEOUT_MILLIS
                }
                request?.urlParam()?.forEach { (key, value) ->
                    parameter(key, value)
                }
            }

            if (response.status != HttpStatusCode.OK) {
                throw HttpError("Failed to get data. status=${response.status.value}")
            }

            response.body()
        } catch (e: Exception) {
            when (e) {
                is HttpError -> throw e
                else -> throw HttpError("Failed to get data. cause: ${e.message}")
            }
        }
    }

    override suspend fun post(endpoint: String, request: Request): Map<String, Any?> {
        return try {
            val response = client.post(endpoint) {
                timeout {
                    requestTimeoutMillis = TIMEOUT_MILLIS
                }
                contentType(ContentType.Application.Json)
                request.urlParam()?.forEach { (key, value) ->
                    parameter(key, value)
                }
                request.body()?.let { setBody(it) }
            }

            if (response.status != HttpStatusCode.OK) {
                throw HttpError("Failed to post data. status=${response.status.value}")
            }

            response.body()
        } catch (e: Exception) {
            when (e) {
                is HttpError -> throw e
                else -> throw HttpError("Failed to post data. cause: ${e.message}")
            }
        }
    }
}
