package jp.hotdrop.considercline.model

sealed class HttpClientState {
    object useFakeHttp : HttpClientState()
    data class useHttpWithDebugLog(val endpoint: String) : HttpClientState()
    data class useHttpNoneLog(val endpoint: String) : HttpClientState()
}