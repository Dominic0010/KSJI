package com.example.ksji833

class MessageModel(
    var senderId: String,
    var receiverId: String,
    var message: String,
    var date: String = System.currentTimeMillis().toString(),
    var type: String
)