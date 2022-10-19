package com.example.chatandroid.data.model

class User {
    var name: String? = null
    var email:String? = null
    var uid:String? = null
    var photoName:String? = null
    var token:String? = null

    constructor(){
        
    }
    
    constructor(name:String?, email:String?,uid:String?, photoName:String?, token:String? = null){
        this.name = name
        this.email = email
        this.uid = uid
        this.photoName = photoName
        this.token = token
    }

}