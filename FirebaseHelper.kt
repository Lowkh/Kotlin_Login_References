package np.ict.mad.navigationui

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseHelper{

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private fun toEmail(username:String): String {
        return if(username.contains("@")){
            username
        }else{
            "$username@navigationui.com" //fake email
        }
    }

    suspend fun signIn(username: String, password: String):Boolean {
        return try{
            val email = toEmail(username)
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user!= null
        } catch (e:Exception){
            Log.e("FirebaseHelper", "Login Failed!", e)
            false
        }
    }

    suspend fun signUp(username: String, password: String): Boolean
    {
        return try{
            val email = toEmail(username)
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user!= null
        } catch (e:Exception){
            Log.e("FirebaseHelper", "Login Failed!", e)
            false
        }

    }
}