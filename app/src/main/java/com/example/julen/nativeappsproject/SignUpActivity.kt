package com.example.julen.nativeappsproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.julen.nativeappsproject.encription.EncriptionServices
import com.example.julen.nativeappsproject.extensions.startHomeActivity
import com.example.julen.nativeappsproject.storage.SharedPreferencesManager
import kotlinx.android.synthetic.main.signup_activity.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)
    }

    override fun onStart() {
        super.onStart()
        signup_button.setOnClickListener {
            if(attemptToSignUp()){
                startHomeActivity()
            }else{
                notSamePasswords()
            }}
    }

    private fun notSamePasswords() {
        error.visibility = View.VISIBLE
    }

    private fun attemptToSignUp(): Boolean {

        var password = passwordText.text.toString()
        var confirmpassword = confirmpasswordText.text.toString()

        if(password.equals(confirmpassword)){
            var es = EncriptionServices(this)
            es.createMasterKey()


            var encryptedPassword = with(EncriptionServices(this)){
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
