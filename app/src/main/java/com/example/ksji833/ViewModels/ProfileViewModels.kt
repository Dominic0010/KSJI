package com.example.ksji833.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ksji833.AppRepo.AppRepo
import com.example.ksji833.UserInfoModel

class ProfileViewModel : ViewModel() {
    private var appRepo = AppRepo.StaticFunction.getInstance()

    public fun getUser():LiveData<UserInfoModel>{
        return appRepo.getUser()
    }

    fun updateStatus(status: String) {
        appRepo.updateStatus(status)

    }

    fun updateName(userName: String?) {
        appRepo.updateName(userName)

    }

    fun updateImage(imagePath: String) {
        appRepo.updateImage(imagePath)

    }
}