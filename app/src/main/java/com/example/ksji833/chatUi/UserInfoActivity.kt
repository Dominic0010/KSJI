package com.example.ksji833.chatUi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ksji833.UserInfoModel
import com.example.ksji833.databinding.ActivityUserInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserInfoActivity: AppCompatActivity() {

    private  var userId:String? = null
    private lateinit var activityUserInfoBinding: ActivityUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        activityUserInfoBinding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(activityUserInfoBinding.root)

        supportActionBar!!.title = "User Info"

        userId = intent.getStringExtra("userId")
        getUserData(userId)
    }
    private fun getUserData(userId:String?){
        val databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId!!)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val userInfoModel = snapshot.getValue(UserInfoModel::class.java)
                    activityUserInfoBinding.userModel = userInfoModel

                    if(userInfoModel!!.name!!.contains("")){
                        val split = userInfoModel.name!!.split("")
                        activityUserInfoBinding.txtProfileFName.text = split[0]
                        activityUserInfoBinding.txtProfileLName.text = split[0]
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}