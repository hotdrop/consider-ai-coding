package jp.hotdrop.considercline.repository.remote

interface HttpClient {
    suspend fun <reified T> get(endpoint: String, params: Map<String, Any?> = emptyMap()): T
    suspend fun <reified T, reified R> post(endpoint: String, body: T): R
}
