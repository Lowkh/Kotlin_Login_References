package np.ict.mad.navigationui

import android.content.Context
import android.provider.Settings.Global.putString
import androidx.core.content.edit

class SharedPreferencesHelper (context: Context){

    private val prefs = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)

    suspend fun saveUser(username:String, password:String): Boolean{
        prefs.edit {
            putString("Stored_username", username)
            putString("Stored_password", password)
        }
        return true
    }
    suspend fun isValidUser(username: String, password: String): Boolean {
        val storedUser = prefs.getString("Stored_username",null)
        val storedPassword = prefs.getString("Stored_password", null)

        return storedUser == username && storedPassword == password
    }

}