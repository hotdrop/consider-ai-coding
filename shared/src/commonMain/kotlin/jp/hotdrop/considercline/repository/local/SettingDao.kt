package jp.hotdrop.considercline.repository.local

import com.russhwolf.settings.Settings

interface SettingDao {
    suspend fun getUserId(): String?
    suspend fun getNickName(): String?
    suspend fun getEmail(): String?
    suspend fun save(userId: String, nickName: String? = null, email: String? = null)
}

class SettingDaoImpl(private val settings: Settings) : SettingDao {
    companion object {
        private const val USER_ID_KEY = "key001"
        private const val NICK_NAME_KEY = "key002"
        private const val EMAIL_KEY = "key003"
    }

    override suspend fun getUserId(): String? = settings.getStringOrNull(USER_ID_KEY)
    override suspend fun getNickName(): String? = settings.getStringOrNull(NICK_NAME_KEY)
    override suspend fun getEmail(): String? = settings.getStringOrNull(EMAIL_KEY)

    override suspend fun save(userId: String, nickName: String?, email: String?) {
        settings.putString(USER_ID_KEY, userId)
        nickName?.let { settings.putString(NICK_NAME_KEY, it) }
        email?.let { settings.putString(EMAIL_KEY, it) }
    }
}
