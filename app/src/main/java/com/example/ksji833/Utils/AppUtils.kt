package com.example.ksji833.Utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class AppUtils {

    fun getUID(): String?{
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.uid
    }
}