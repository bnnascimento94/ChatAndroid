package com.example.chatandroid.data.model

class Message {
    var message:String? =  null
    var senderId: String? = null
    var photoUserSender: String? = null
    var photoMessage: String? = null


    constructor(){}

    constructor(message:String?, senderId:String?, photoUserSender: String?, photoMessage: String?){
        this.message = message
        this.senderId = senderId
        this.photoUserSender = photoUserSender
        this.photoMessage =photoMessage
    }
}