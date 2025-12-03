package np.ict.mad.navigationui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class DataStoreHelper(private val context: Context) {
    companion object{
        val USERNAME_KEY = stringPreferencesKey("ds_username")
        val PASSWORD_KEY = stringPreferencesKey("ds_password")
    }

    suspend fun saveUser(username: String, password: String): Boolean{
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
            preferences[PASSWORD_KEY] = password
        }
        return true
    }

    suspend fun isValidUser(username: String, password: String): Boolean{
        val preference = context.dataStore.data.first()

        val StoredUsername = preference[USERNAME_KEY]
        val StoredPassword = preference[PASSWORD_KEY]
        return StoredUsername == username && StoredPassword == password

    }

}