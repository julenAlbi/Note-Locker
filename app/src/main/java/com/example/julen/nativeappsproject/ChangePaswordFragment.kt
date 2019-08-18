package com.example.julen.nativeappsproject

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.julen.nativeappsproject.encription.EncryptionServices
import com.example.julen.nativeappsproject.extensions.toast
import com.example.julen.nativeappsproject.storage.SharedPreferencesManager
import kotlinx.android.synthetic.main.change_pasword_fragment.*


class ChangePaswordFragment : Fragment() {

//    private lateinit var viewModel: ChangePaswordViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.change_pasword_fragment, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(ChangePaswordViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    /**
     * If the password entered aren't the same, it will display an error, else a new activity will be launched.
     */
    override fun onStart() {
        super.onStart()

        setHasOptionsMenu(true)

        changepasswordbutton.setOnClickListener {
            if(passwordChangeble()){
                val es = EncryptionServices(context!!)
                val encryptedPassword = with(es){
                    encrypt(newpassword.text.toString())
                }
                SharedPreferencesManager(context!!).savePassword(encryptedPassword)
                context!!.toast("Password changed")
                activity!!.finish()
            }else{
                errorlabel.visibility = View.VISIBLE
            }
        }
    }

    private fun passwordChangeble(): Boolean{
        if(!newpassword.text.toString().equals(newpassword2.text.toString())){
            context!!.toast("Passwords aren't equals" + newpassword.text.toString() + " " + newpassword2.text.toString())
            return false
        }
        if (newpassword.text.toString().length == 0){
            context!!.toast("New password can't be empty")
            return false
        }
        val sharedPreferencesManager= SharedPreferencesManager(context!!)
        return EncryptionServices(context!!).decrypt(sharedPreferencesManager.getPassword()) == oldpassword.text.toString()
    }

    companion object {
        fun newInstance() = ChangePaswordFragment()
    }

}
