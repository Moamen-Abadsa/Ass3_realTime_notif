package com.example.realTime_fcm

import java.io.Serializable

class Book: Serializable {
    var id:String=""
    var name:String=""
    var auther:String=""
    var date:String=""
    var rate:Double=0.0
    var price:Int=0
    var image:String?=null
    constructor()
    constructor(id:String,name:String,auther:String,date:String,rate:Double,price:Int,iamge:String){
        this.name=name
        this.auther=auther
        this.date=date
        this.rate=rate
        this.price=price
        this.image=image
        this.id=id
    }
}