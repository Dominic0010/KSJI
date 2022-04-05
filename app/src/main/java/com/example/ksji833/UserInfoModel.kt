package com.example.ksji833

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

    constructor(){}

    constructor(name: String?, email:String?, uid:String?){
        this.name = name
        this.email = email
        this .uid = uid
    }
}