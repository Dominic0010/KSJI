package com.example.ksji833

import java.lang.StringBuilder

object Common {
    fun buildWelcomeMessage(): String {
        return StringBuilder("Welcome, ")
            .append(currentUser!!.name)
            .append(" ")
            .append(currentUser.email)
            .toString()

    }

    val currentUser: UserInfoModel? = null
    val USER_INFO_REFERENCE: String = "UserInfo"
}