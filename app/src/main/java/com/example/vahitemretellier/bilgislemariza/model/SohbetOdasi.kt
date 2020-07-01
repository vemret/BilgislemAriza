package com.example.vahitemretellier.bilgislemariza.model

class SohbetOdasi{
    var sohbetOdasi_id:String?=null
    var oda_seviye:String?=null
    var sohbet_odasi_mesajlari:List<SohbetMesaj>?=null
    constructor(){}
    constructor(sohbetOdasi_id:String,oda_seviye:String,sohbet_odasi_mesajlari:List<SohbetMesaj>){
        this.sohbetOdasi_id=sohbetOdasi_id
        this.oda_seviye=oda_seviye
        this.sohbet_odasi_mesajlari=sohbet_odasi_mesajlari
    }
}