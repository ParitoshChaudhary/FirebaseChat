package com.example.firebasechat.models

class Message() {

    var id: String? = null
    var text: String? = null
    var name: String? = null

    constructor(id: String, text: String, name: String){
        this.id = id
        this.name = name
        this.text = text
    }

}