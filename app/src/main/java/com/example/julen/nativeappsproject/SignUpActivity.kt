package com.example.julen.nativeappsproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.julen.nativeappsproject.encription.EncryptionServices
import com.example.julen.nativeappsproject.extensions.startHomeActivity
import com.example.julen.nativeappsproject.storage.SharedPreferencesManager
import kotlinx.android.synthetic.main.signup_activity.*

/**
 * The activity that is going to be used sign up when the app is launched at the first time.
 * First it will encrypt the password and it will save it in the shared preferences.
 */
class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)
    }

    /**
     * If the password entered aren't the same, it will display an error, else a new activity will be launched.
     */
    override fun onStart() {
        super.onStart()
        signup_button.setOnClickListener {
            if(attemptToSignUp()){
                startHomeActivity()
            }else{
                notSamePasswords()
            }
        }
    }

    /**
     * If the tow password are not the same, it will display an error
     */
    private fun notSamePasswords() {
        error.visibility = View.VISIBLE
    }

    /**
     * Before save the password, it will check if the two password are the same,
     * if are the same it will encrypt the password and save it in the shared preferences.
     * @return true if the password has been saved, false else.
     */
    private fun attemptToSignUp(): Boolean {

        val password = passwordText.text.toString()
        val confirmpassword = confirmpasswordText.text.toString()

        if(password.equals(confirmpassword)){
            val es = EncryptionServices(applicationContext)

            val encryptedPassword = with(es){
                createMasterKey()
                encrypt(password)
            }
            SharedPreferencesManager(this).savePassword(encryptedPassword)
            return true
        }else{
            return false
        }

    }
}
