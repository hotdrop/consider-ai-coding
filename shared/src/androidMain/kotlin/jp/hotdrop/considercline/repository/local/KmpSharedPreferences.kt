package jp.hotdrop.considercline.repository.local

actual class KmpSharedPreferences {
    actual fun getUserId(): String? {
        // TODO SharedPrefs入れる
        TODO("Not yet implemented")
    }

    actual fun saveUserId(newVal: String) {
        TODO("Not yet implemented")
    }
    actual fun getNickName(): String? {
        TODO("Not yet implemented")
    }
    actual fun saveNickName(newVal: String) {
        TODO("Not yet implemented")
    }
    actual fun getEmail(): String? {
        TODO("Not yet implemented")
    }
    actual fun saveEmail(newVal: String) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val USER_ID_KEY = "key001"
        private const val NICK_NAME_KEY = "key002"
        private const val EMAIL_KEY = "key003"
    }
}