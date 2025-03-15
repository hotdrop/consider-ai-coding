package jp.hotdrop.considercline.repository.remote.models

interface Request {
    fun urlParam(): Map<String, Any?>?
    fun body(): Map<String, Any?>?
}
