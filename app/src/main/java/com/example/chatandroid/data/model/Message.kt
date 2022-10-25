package com.example.chatandroid.data.model

class Message {
    var messageTitledDate:String? = null
    var message:String? =  null
    var senderId: String? = null
    var photoUserSender: String? = null
    var photoMessage: String? = null
    var time: Long? = null
    var dataTexto : String? = null


    constructor(){}

    constructor(messageTitledDate:String? = null,message:String?, senderId:String?, photoUserSender: String?, photoMessage: String?, time:Long?, dataTexto: String? = null){
        this.messageTitledDate = messageTitledDate
        this.message = message
        this.senderId = senderId
        this.photoUserSender = photoUserSender
        this.photoMessage =photoMessage
        this.time = time
        this.dataTexto = dataTexto
    }
}