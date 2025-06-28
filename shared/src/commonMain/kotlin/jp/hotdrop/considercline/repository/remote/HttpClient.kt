package jp.hotdrop.considercline.repository.remote

import jp.hotdrop.considercline.repository.remote.models.Request

interface HttpClient {
    suspend fun get(endpoint: String, request: Request?): Map<String, Any?>
    suspend fun post(endpoint: String, request: Request): Map<String, Any?>
}
