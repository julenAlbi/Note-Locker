package com.example.julen.nativeappsproject.authentication

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatDialogFragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.julen.nativeappsproject.R
import com.example.julen.nativeappsproject.model.Note
import kotlinx.android.synthetic.main.dialogfragment.*

/**
 * This class will display a authentication dialog.
 */

class AuthenticationDialog() : AppCompatDialogFragment() {

    //This lambda function contains a function to verify the password.
    var passwordVerificationListener: ((password : String) -> Boolean)? = null
    var authenticationSuccessListener: ( () -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialogfragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.setTitle(getString(R.string.authentication_title))
        OkButton.setOnClickListener {
            if(verifyPassword(password.text.toString())){
                authenticationSuccessListener?.invoke()
                dismiss()
            }else
                dismiss()
        }
        CancelButton.setOnClickListener {
            dismiss()
        }
    }
    /**
     * Checks whether the current entered password is correct, and dismisses the the dialog and
     * let's the activity know about the result.
     */
    private fun verifyPassword(inputPassword: String): Boolean {
        if (!checkPassword(inputPassword)) {
            return false
        }
        password.append("")
        return true
    }

    /**
     * @return true if `password` is correct, false otherwise
     */
    private fun checkPassword(password: String): Boolean {
        return passwordVerificationListener?.invoke(password) ?: false
    }
}