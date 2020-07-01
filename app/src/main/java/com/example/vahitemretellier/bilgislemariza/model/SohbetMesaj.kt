package com.example.vahitemretellier.bilgislemariza.model

class SohbetMesaj {
    var mesajlar: String?=null
    var kullanici_id: String?=null
    var timestamp: String?=null
    var isim: String?=null
    var birim: String?=null

    constructor() {}

    constructor( mesajlar: String, kullanici_id: String, timestamp: String, isim: String, birim: String){
        this.mesajlar=mesajlar
        this.kullanici_id=kullanici_id
        this.timestamp=timestamp
        this.isim=isim
        this.birim=birim
    }
}