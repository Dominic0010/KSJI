package com.example.ksji833.AppRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ksji833.UserInfoModel
import com.example.ksji833.Utils.AppUtils
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class AppRepo {

    private var liveData: MutableLiveData<UserInfoModel>? = null
    private lateinit var databaseReference: DatabaseReference
    private val appUtil = AppUtils()

    object StaticFunction{
        private var instance: AppRepo? = null
        fun getInstance():AppRepo{
            if (instance==null)
                instance= AppRepo()

            return instance!!
        }
    }


    fun getUser():LiveData<UserInfoModel>{

        if(liveData==null)
            liveData= MutableLiveData()
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(appUtil.getUID()!!)
        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val userInfoModel=snapshot.getValue(UserInfoModel::class.java)
                    liveData!!.postValue(userInfoModel!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return liveData!!
    }

    fun updateStatus(status: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("User").child(appUtil.getUID()!!)

        val map = mapOf<String, Any>("status" to status)
        databaseReference.updateChildren(map)

    }

    fun updateName(userName: String?) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("User").child(appUtil.getUID()!!)

            val map = mapOf<String, Any>("name" to userName!!)
        databaseReference.updateChildren(map)

    }

    fun updateImage(imagePath: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("User").child(appUtil.getUID()!!)

        val map = mapOf<String, Any>("avatar" to imagePath!!)
        databaseReference.updateChildren(map)

    }


}