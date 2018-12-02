package com.example.julen.nativeappsproject

import com.example.julen.nativeappsproject.model.Note

interface FragmentCommunication {
    fun changeFragment(note :  Note? = null)
}