package jp.hotdrop.considercline.repository.local

class UserDao(
    private val sharedPreferences: KmpSharedPreferences
) {
    suspend fun getUserId(): String? = sharedPreferences.getUserId()
    suspend fun getNickName(): String? = sharedPreferences.getNickName()
    suspend fun getEmail(): String? = sharedPreferences.getEmail()

    suspend fun save(userId: String, nickName: String?, email: String?) {
        sharedPreferences.saveUserId(userId)
        nickName?.let { sharedPreferences.saveNickName(it) }
        email?.let { sharedPreferences.saveEmail(it) }
    }
}
