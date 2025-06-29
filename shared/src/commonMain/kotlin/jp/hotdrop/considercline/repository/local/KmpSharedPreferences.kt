package jp.hotdrop.considercline.repository.local

interface KmpSharedPreferences {
    suspend fun getUserId(): String?
    suspend fun saveUserId(newVal: String)
    suspend fun getNickName(): String?
    suspend fun saveNickName(newVal: String)
    suspend fun getEmail(): String?
    suspend fun saveEmail(newVal: String)
    suspend fun getPoint(): Int
    suspend fun savePoint(newVal: Int)
    suspend fun getJwt(): String?
    suspend fun saveJwt(newVal: String)
    suspend fun getRefreshToken(): String?
    suspend fun saveRefreshToken(newVal: String)
}