package jp.hotdrop.considercline.repository.local

expect class KmpSharedPreferences {
    fun getUserId(): String?
    fun saveUserId(newVal: String)
    fun getNickName(): String?
    fun saveNickName(newVal: String)
    fun getEmail(): String?
    fun saveEmail(newVal: String)
}