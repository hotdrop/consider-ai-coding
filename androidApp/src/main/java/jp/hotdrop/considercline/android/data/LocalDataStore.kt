package jp.hotdrop.considercline.android.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import jp.hotdrop.considercline.repository.local.KmpSharedPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalDataStore(private val context: Context) : KmpSharedPreferences {

    private val USER_ID_KEY = stringPreferencesKey("key001")
    private val NICK_NAME_KEY = stringPreferencesKey("key002")
    private val EMAIL_KEY = stringPreferencesKey("key003")
    private val FAKE_LOCAL_STORE_POINT_KEY = intPreferencesKey("key004")
    private val JWT_KEY = stringPreferencesKey("key005")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("key006")

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override suspend fun getUserId(): String? {
        return context.dataStore.data.map { pref ->
            pref[USER_ID_KEY]
        }.first()
    }

    override suspend fun saveUserId(newVal: String) {
        context.dataStore.edit { settings ->
            settings[USER_ID_KEY] = newVal
        }
    }

    override suspend fun getNickName(): String? {
        return context.dataStore.data.map { pref ->
            pref[NICK_NAME_KEY]
        }.first()
    }

    override suspend fun saveNickName(newVal: String) {
        context.dataStore.edit { settings ->
            settings[NICK_NAME_KEY] = newVal
        }
    }

    override suspend fun getEmail(): String? {
        return context.dataStore.data.map { pref ->
            pref[EMAIL_KEY]
        }.first()
    }

    override suspend fun saveEmail(newVal: String) {
        context.dataStore.edit { settings ->
            settings[EMAIL_KEY] = newVal
        }
    }

    override suspend fun getPoint(): Int {
        return context.dataStore.data.map { pref ->
            pref[FAKE_LOCAL_STORE_POINT_KEY]
        }.first() ?: 0
    }

    override suspend fun savePoint(newVal: Int) {
        context.dataStore.edit { settings ->
            settings[FAKE_LOCAL_STORE_POINT_KEY] = newVal
        }
    }

    override suspend fun getJwt(): String? {
        return context.dataStore.data.map { pref ->
            pref[JWT_KEY]
        }.first()
    }

    override suspend fun saveJwt(newVal: String) {
        context.dataStore.edit { settings ->
            settings[JWT_KEY] = newVal
        }
    }

    override suspend fun getRefreshToken(): String? {
        return context.dataStore.data.map { pref ->
            pref[REFRESH_TOKEN_KEY]
        }.first()
    }

    override suspend fun saveRefreshToken(newVal: String) {
        context.dataStore.edit { settings ->
            settings[REFRESH_TOKEN_KEY] = newVal
        }
    }
}