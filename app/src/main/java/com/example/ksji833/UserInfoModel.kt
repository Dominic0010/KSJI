package com.example.ksji833

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

//class UserInfoModel(name: String, email: String, uid: String)
// {
//    var name:String=""
//    var email:String=""
//    var uid:String=""

//}

class UserInfoModel{
    var name: String?= null
    var email:String?= null
    var uid: String?= null
    var avatar:String?=null
    var status:String?=null

    //    var Upload_imageUri : Uri? = null

    constructor(){}

    constructor(name: String?, email:String?, uid: String?, avatar: String?,status:String?){
        this.name = name
        this.email = email
        this .uid = uid
        this.avatar = avatar
        this.status = status
//        this.Upload_imageUri= Upload_imageUri
    }

//    companion object{
//        @JvmStatic
//        @BindingAdapter("imageUrl")
//        fun loadImage(view: CircleImageView, imageUrl: String?) {
//            imageUrl?.let {
//                Glide.with(view.context).load(imageUrl).into(view)
//            }
//        }
//    }

}