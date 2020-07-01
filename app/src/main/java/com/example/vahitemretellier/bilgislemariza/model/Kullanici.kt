package com.example.vahitemretellier.bilgislemariza.model

class Kullanici {
    var isim: String? = null
    var birim: String? = null
    var seviye: String? = null
    var kullanici_id: String? = null

    constructor(isim:String , birim:String , seviye:String , kullanici_id:String){

        this.isim = isim
        this.birim=birim
        this.seviye=seviye
        this.kullanici_id=kullanici_id

    }

    constructor(){}

}
