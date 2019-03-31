package com.example.devtalks.models

class ChatMessage(val id:String,val text: String,val fromId:String,val toId:String,val Timestamp:Long ){
    constructor():this("","","","",-1)
}