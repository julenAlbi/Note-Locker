package com.example.julen.nativeappsproject.authentication

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.julen.nativeappsproject.R
import kotlinx.android.synthetic.main.dialogfragment.*

/**
 * This class will display a authentication dialog.
 */

class authenticationDialog : AppCompatDialogFragment() {

    //This lambda function contains a function to verify the password.
    var passwordVerificationListener: ((password : String) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialogfragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.setTitle(getString(R.string.authentication_title))
        OkButton.setOnClickListener {
            passwordVerificationListener?.invoke(password.text.toString())
            dismiss()
        }
        CancelButton.setOnClickListener {
            dismiss()
        }
    }
}